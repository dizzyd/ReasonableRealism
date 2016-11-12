package com.draco18s.farming;

import org.apache.logging.log4j.Logger;

import com.draco18s.farming.block.BlockCropWeeds;
import com.draco18s.farming.block.BlockCropWinterWheat;
import com.draco18s.farming.block.BlockSaltOre;
import com.draco18s.farming.block.BlockTanner;
import com.draco18s.farming.entities.TileEntityTanner;
import com.draco18s.farming.entities.capabilities.IMilking;
import com.draco18s.farming.entities.capabilities.MilkStorage;
import com.draco18s.farming.item.ItemAchieves;
import com.draco18s.farming.item.ItemButcherKnife;
import com.draco18s.farming.item.ItemHydrometer;
import com.draco18s.farming.item.ItemThermometer;
import com.draco18s.farming.item.ItemWinterSeeds;
import com.draco18s.farming.util.AnimalUtil;
import com.draco18s.farming.util.CropManager;
import com.draco18s.farming.util.EnumFarmAchieves;
import com.draco18s.farming.util.FarmingAchievements;
import com.draco18s.farming.world.WorldGenerator;
import com.draco18s.hardlib.EasyRegistry;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.blockproperties.ores.EnumOreType;
import com.draco18s.hardlib.internal.CropWeatherOffsets;
import com.draco18s.hardlib.util.CapabilityUtils;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid="harderfarming", name="HardFarming", version="{@version:farm}", dependencies = "required-after:hardlib")
public class FarmingBase {
	@Instance("harderfarming")
	public static FarmingBase instance;
	
	@SidedProxy(clientSide="com.draco18s.farming.client.ClientProxy", serverSide="com.draco18s.farming.CommonProxy")
	public static CommonProxy proxy;
	
	public static Logger logger;
	
	public static Block winterWheat;
	public static Block weeds;
	public static Block saltOre;
	public static Block tanningRack;

	public static Item winterWheatSeeds;
	public static Item thermometer;
	public static Item rainmeter;
	public static Item rawLeather;
	public static Item rawSalt;
	public static Item saltPile;
	public static Item butcherKnife;
	public static Item itemAchievementIcons;

	public static Configuration config;

	
	@CapabilityInject(IMilking.class)
	public static final Capability<IMilking> MILKING_CAPABILITY = null;

	public static final ResourceLocation MILK_ID = new ResourceLocation("harderfarming", "MilkCap");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		logger = event.getModLog();
		HardLibAPI.hardCrops = new CropManager();
    	HardLibAPI.animalManager = new AnimalUtil();
    	FarmingEventHandler.doSlowCrops = config.getBoolean("EnableCropSlowing", "CROPS", true, "Enables or disables the slowdown of crop growth.\nIf enabled, base probability is 10% as frequent as vanilla (ten times slower).\nNote: please disable Gany's Surface's snow accumulation, if it is\ninstalled (mine results in a smoother variation between blocks).\n");
    	FarmingEventHandler.doBiomeCrops = config.getBoolean("EnableCropSlowingByBiome", "CROPS", true, "Enables or disables the crop growth based on biome information (which is effected by seasons,\nif enabled and ignored if slow crops is disabled). Most (vanilla) biomes have some semblance of a\ngrowing season, though it will be harder to grow food in the cold and dry biomes. Growing plants\ninside uses an effective temperature halfway closer to the ideal value.  For extreme biomes\nthis might be required!\nIf disabled, base slowdown probability is used instead.\n");
    	FarmingEventHandler.cropsWorst = config.getInt("SlowByBiomeLowerBound", "CROPS", 16, 8, 96, "Configures the worst possible growth rate for biome based crop growth.\nIn the worst possible conditions, the chance that crops will grow will not drop\nbelow 100/(value + 10) %\nGenerally speaking this occurs in the frozen biomes during the winter, most notably Cold Taiga.\nThere should be no need for this value to exceed 16 for any biome other than Cold Taiga (50+)\nand Cold Beach (20+).\n");
    	
		
		weeds = new BlockCropWeeds();
		EasyRegistry.registerBlock(weeds, "crop_weeds");
		winterWheat = new BlockCropWinterWheat();
		EasyRegistry.registerBlock(winterWheat, "crop_winter_wheat");
		
		winterWheatSeeds = new ItemWinterSeeds(winterWheat, Blocks.FARMLAND);
		EasyRegistry.registerItem(winterWheatSeeds, "seeds_winter_wheat");
		
    	saltOre = new BlockSaltOre();
    	EasyRegistry.registerBlockWithItem(saltOre, "saltore");
    	tanningRack = new BlockTanner();
    	EasyRegistry.registerBlockWithItem(tanningRack, "tanner");
    	GameRegistry.registerTileEntity(TileEntityTanner.class, "tanning_rack");

    	thermometer = new ItemThermometer();
    	EasyRegistry.registerItem(thermometer, "thermometer");
    	rainmeter = new ItemHydrometer();
    	EasyRegistry.registerItem(rainmeter, "hydrometer");
    	rawLeather = (new Item()).setCreativeTab(CreativeTabs.MATERIALS);
    	EasyRegistry.registerItem(rawLeather, "rawleather");
    	rawSalt = (new Item()).setCreativeTab(CreativeTabs.MATERIALS);
    	EasyRegistry.registerItem(rawSalt, "rawsalt");
    	saltPile = (new Item()).setCreativeTab(CreativeTabs.MATERIALS);
    	EasyRegistry.registerItem(saltPile, "saltpile");
    	
    	butcherKnife = new ItemButcherKnife(ToolMaterial.IRON);
    	EasyRegistry.registerItem(butcherKnife, "butcherknife");
    	
    	itemAchievementIcons = new ItemAchieves();
    	EasyRegistry.registerItemWithVariants(itemAchievementIcons, "achieve_icons", EnumFarmAchieves.KILL_WEEDS);
    	
    	((AnimalUtil) HardLibAPI.animalManager).parseConfig(config);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		FarmingEventHandler.doRawLeather = config.getBoolean("doRawLeather", "ANIMALS", true, "Raw leather (rawhide) requires curing on a tanning rack before it can be used.\n");
    	config.save();
    	
    	if(Loader.isModLoaded("harderores")) {
    		HardLibAPI.oreMachines.addMillRecipe(new ItemStack(rawSalt), new ItemStack(saltPile, 2));
    		HardLibAPI.oreMachines.addSiftRecipe(new ItemStack(saltPile), new ItemStack(saltPile));
    	}
    	
    	GameRegistry.registerWorldGenerator(new WorldGenerator(), 2);
    	OreDictionary.registerOre("dustSalt", saltPile);
    	
    	CropWeatherOffsets off = new CropWeatherOffsets(0,0,(int) 0.5f,0);
    	HardLibAPI.hardCrops.putCropWeather(Blocks.PUMPKIN_STEM, off);//primarily october growth
		off = new CropWeatherOffsets(0,0,0,0);
		HardLibAPI.hardCrops.putCropWeather(Blocks.WHEAT, off);//no offsets!
		//off = new CropWeatherOffsets(0.8f,0.2f,0,0);
		//HardLibAPI.hardCrops.putCropWeather(winterWheat, off);//grows best when cold
		off = new CropWeatherOffsets(-0.4f,0,0,0);
		HardLibAPI.hardCrops.putCropWeather(Blocks.MELON_STEM, off);//cold sensitive
		off = new CropWeatherOffsets(0.7f,0,0,0);
		HardLibAPI.hardCrops.putCropWeather(Blocks.POTATOES, off);//potatoes are a "cool season" crop
		off = new CropWeatherOffsets(0.1f,0.2f,0,0);
		HardLibAPI.hardCrops.putCropWeather(Blocks.CARROTS, off);//carrots take 4 months to mature, ideal growth between 60 and 70 F
		off = new CropWeatherOffsets(-0.4f,-0.3f,0,0);
		HardLibAPI.hardCrops.putCropWeather(Blocks.REEDS, off);//reeds grow warm and wet
		off = new CropWeatherOffsets(0.25f,0.1f,0,0);
		HardLibAPI.hardCrops.putCropWeather(Blocks.BEETROOTS, off);//beets grow cool and slightly dry
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new FarmingEventHandler());
		CapabilityManager.INSTANCE.register(IMilking.class, new MilkStorage(), new MilkStorage.Factory());
		FarmingAchievements.addCoreAchievements();
	}
	
	public static IMilking getMilkData(EntityLivingBase entity) {
		return CapabilityUtils.getCapability(entity, MILKING_CAPABILITY, null);
	}
}
