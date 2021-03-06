package com.draco18s.ores.inventory;

import javax.annotation.Nullable;

import com.draco18s.hardlib.api.internal.CommonContainer;
import com.draco18s.hardlib.api.internal.inventory.SlotOutput;
import com.draco18s.ores.entities.TileEntityPackager;
import com.draco18s.ores.entities.TileEntitySifter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerPackager extends CommonContainer {
	TileEntityPackager tileEntity;

	public ContainerPackager(InventoryPlayer inventory, TileEntityPackager te) {
		super(3);
		tileEntity = te;
		IItemHandler inven = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		addSlotToContainer(new SlotPackaged(inven, 0, 71, 13));
		addSlotToContainer(new SlotPackaged(inven, 1, 89, 13));
		addSlotToContainer(new SlotOutput(inven, 2, 80, 58));
		bindPlayerInventory(inventory);
	}
}
