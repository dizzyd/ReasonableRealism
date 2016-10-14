package com.draco18s.ores.block.ore;

import java.awt.Color;
import java.util.Random;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.blockproperties.EnumOreType;
import com.draco18s.hardlib.blockproperties.Props;
import com.draco18s.ores.OresBase;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHardIron extends BlockHardOreBase {
	Color color = new Color(0xd8af93);

	public BlockHardIron() {
		super(EnumOreType.IRON, 1);
		setHardness(6.0f);
		setHarvestLevel("pickaxe", 1);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 1 + fortune + random.nextInt(fortune+(state.getValue(Props.ORE_DENSITY))/6+1);
	}
	
	@Override
	public Color getProspectorParticleColor(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		return color;
	}
	
	/*@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
		OresBase.logger.log(Level.INFO, "Iron at " + pos + ", " + state);
    }*/
}
