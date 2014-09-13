package vexatos.tgregworks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tconstruct.library.TConstructCreativeTab;
import vexatos.tgregworks.integration.TGregRecipeRegistry;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.integration.TGregRegistry;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.proxy.CommonProxy;
import vexatos.tgregworks.reference.Mods;

/**
 * @author Vexatos
 */
@Mod(modid = Mods.TGregworks, name = Mods.TGregworks_NAME, version = "0.0.1", dependencies = "required-after:" + Mods.TConstruct + ";required-after:" + Mods.GregTech)
public class TGregworks {

	public static Logger log = LogManager.getLogger(Mods.TGregworks);
	public Configuration config;

	@Mod.Instance(value = Mods.TGregworks)
	public TGregworks instance;

	@SidedProxy(clientSide = "vexatos.tgregworks.proxy.ClientProxy", serverSide = "vexatos.tgregworks.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static TGregRegistry registry;
	public static TGregRecipeRegistry recipes;

	public static ItemTGregPart toolParts;

	public static TConstructCreativeTab tab = new TConstructCreativeTab("tabTGregworks");

	public TGregworks(){
		if(Loader.isModLoaded(Mods.TConstruct)){
			log.info("Hey, TConstruct, I'm here as well! May I join you?");
			LogManager.getLogger(Mods.TConstruct).info("Sure, let's take over the world together!");
		}
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		config.save();

		registry = new TGregRegistry();
		registry.registerToolParts();
		recipes = new TGregRecipeRegistry();

		toolParts = new ItemTGregPart();

		GameRegistry.registerItem(toolParts, "tGregToolParts");

		{
			ItemStack stack = new ItemStack(toolParts, 1, PartTypes.LargeSwordBlade.metaID);
			NBTTagCompound data = ItemTGregPart.getTagCompound(stack);
			data.setString("material", Materials.Osmiridium.name());
			stack.setTagCompound(data);
			TGregworks.tab.init(stack);
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.addToolRenderMappings();
		recipes.addRecipesForToolBuilder();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
