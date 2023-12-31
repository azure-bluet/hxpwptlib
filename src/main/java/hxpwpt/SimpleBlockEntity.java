package hxpwpt;

import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

// The capacity part is untested
// Also I just do NOT understand some of the literals
public abstract class SimpleBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
	private NonNullList <ItemStack> stacks;
    private final String name;
    private final Component display;
	private final LazyOptional <? extends IItemHandler> [] handlers = SidedInvWrapper.create (this, Direction.values ());
    public class FluidStorage extends FluidTank {
        public FluidStorage (int amount, Predicate <FluidStack> acceptable) {
            super (amount, acceptable);
        }
		@Override
		protected void onContentsChanged () {
			super.onContentsChanged ();
			setChanged ();
            // What does the 2 mean? I don't know and this may cause bugs
            // Seriously
			if (level != null)
			level.sendBlockUpdated (worldPosition, level.getBlockState (worldPosition), level.getBlockState (worldPosition), 2);
		}
    }
	private final FluidTank fluid;
    private final boolean enable_fluid;
    public class EnergyTank extends EnergyStorage {
        public EnergyTank (int capacity, int max_receive, int max_extract, int init) {
            super (capacity, max_receive, max_extract, init);
        }
        @Override
		public int receiveEnergy (int max_receive, boolean simulate) {
			int retval = super.receiveEnergy (max_receive, simulate);
			if (!simulate) {
				setChanged ();
				if (level != null)
				level.sendBlockUpdated (worldPosition, level.getBlockState (worldPosition), level.getBlockState (worldPosition), 2);
			}
			return retval;
		}
		@Override
		public int extractEnergy (int max_extract, boolean simulate) {
			int retval = super.extractEnergy (max_extract, simulate);
			if (!simulate) {
				setChanged ();
				if (level != null)
				level.sendBlockUpdated (worldPosition, level.getBlockState (worldPosition), level.getBlockState (worldPosition), 2);
			}
			return retval;
		}
    }
	private final EnergyTank energy;
    private final boolean enable_energy;
    public interface ItemMovable {
        public boolean movable (int index, ItemStack stack, @Nullable Direction direction);
		public static ItemMovable TRUE = new ItemMovable () {
			public boolean movable (int index, ItemStack stack, @Nullable Direction direction) {
				return true;
			}
		};
		public static ItemMovable FALSE = new ItemMovable () {
			public boolean movable (int index, ItemStack stack, @Nullable Direction direction) {
				return false;
			}
		};
    }
    @Nullable private final ItemMovable can_take, can_place;
	public SimpleBlockEntity (Supplier <BlockEntityType <?>> gets, BlockPos position, BlockState state,
        String name, @Nullable Component display,
        int inventory, @Nullable ItemMovable can_take, @Nullable ItemMovable can_place,
        int fluid_amount, @Nullable Predicate <FluidStack> acceptable_fluid,
        int energy, int energy_receive, int energy_extract, int init_energy) {
		super (gets.get (), position, state);
        this.stacks = NonNullList. <ItemStack> withSize (inventory, ItemStack.EMPTY);
        this.can_take = can_take;
        this.can_place = can_place;
        this.name = name;
        this.display = (display == null) ? (Component.literal (name)) : display;
        if (fluid_amount == -1) {
            this.fluid = null;
            this.enable_fluid = false;
        } else {
            this.fluid = new FluidStorage (fluid_amount, acceptable_fluid);
            this.enable_fluid = true;
        }
        if (energy == -1) {
            this.energy = null;
            this.enable_energy = false;
        } else {
            this.energy = new EnergyTank (energy, energy_receive, energy_extract, init_energy);
            this.enable_energy = true;
        }
	}
    public static record Config (Supplier <BlockEntityType <?>> gets,
        String name, @Nullable Component display,
        int inventory, @Nullable ItemMovable can_take, @Nullable ItemMovable can_place,
        int fluid_amount, @Nullable Predicate <FluidStack> acceptable_fluid,
        int energy, int energy_receive, int energy_extract, int init_energy) {}
	public SimpleBlockEntity (BlockPos pos, BlockState state, Config c) {
		this (c.gets, pos, state, c.name, c.display, c.inventory, c.can_take, c.can_place, c.fluid_amount, c.acceptable_fluid,
			c.energy, c.energy_receive, c.energy_extract, c.init_energy);
	}
	@Override
	public void load (CompoundTag compound) {
		super.load (compound);
		if (!this.tryLoadLootTable (compound)) this.stacks.clear ();
		ContainerHelper.loadAllItems (compound, this.stacks);
		if (this.enable_energy && compound.get ("energy") instanceof IntTag tag)
			energy.deserializeNBT (tag);
		if (this.enable_fluid && compound.get ("fluid") instanceof CompoundTag tag)
			fluid.readFromNBT (tag);
	}
	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional (compound);
		if (!this.trySaveLootTable (compound)) {
			ContainerHelper.saveAllItems (compound, this.stacks);
		}
		if (this.enable_energy) compound.put ("energy", energy.serializeNBT ());
		if (this.enable_fluid) compound.put ("fluid", fluid.writeToNBT (new CompoundTag ()));
	}
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket () {
		return ClientboundBlockEntityDataPacket.create (this);
	}
	@Override
	public CompoundTag getUpdateTag () {
		return this.saveWithFullMetadata ();
	}
	@Override
	public int getContainerSize () {
		return stacks.size ();
	}
	@Override
	public boolean isEmpty () {
		for (ItemStack itemstack : this.stacks)
			if (! itemstack.isEmpty ())
				return false;
		return true;
	}
	@Override
	public Component getDefaultName () {
		return Component.literal (this.name);
	}
	@Override
	public int getMaxStackSize () {
		return 64;
	}
	@Override
	public AbstractContainerMenu createMenu (int id, Inventory inventory) {
		return ChestMenu.threeRows (id, inventory);
	}
	@Override
	public Component getDisplayName () {
        return this.display;
	}
	@Override
	protected NonNullList <ItemStack> getItems () {
		return this.stacks;
	}
	@Override
	protected void setItems (NonNullList <ItemStack> stacks) {
		this.stacks = stacks;
	}
	@Override
	public boolean canPlaceItem (int index, ItemStack stack) {
		if (this.can_place != null) return this.can_place.movable (index, stack, null);
        else return true;
	}
	@Override
	public int [] getSlotsForFace (Direction side) {
		return IntStream.range (0, this.getContainerSize ()) .toArray ();
	}
	@Override
	public boolean canPlaceItemThroughFace (int index, ItemStack stack, @Nullable Direction direction) {
		if (this.can_place != null) return this.can_place.movable (index, stack, direction);
        else return true;
	}
	@Override
	public boolean canTakeItemThroughFace (int index, ItemStack stack, Direction direction) {
		if (this.can_take != null) return this.can_take.movable (index, stack, direction);
        else return true;
	}
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER)
			return handlers[facing.ordinal()].cast();
		if (!this.remove && capability == ForgeCapabilities.ENERGY && this.enable_energy)
			return LazyOptional.of (() -> energy) .cast ();
		if (!this.remove && capability == ForgeCapabilities.FLUID_HANDLER && this.enable_fluid)
			return LazyOptional.of (() -> fluid) .cast ();
		return super.getCapability (capability, facing);
	}
	@Override
	public void setRemoved () {
		super.setRemoved ();
		for (LazyOptional <? extends IItemHandler> handler : handlers)
			handler.invalidate ();
	}
}
