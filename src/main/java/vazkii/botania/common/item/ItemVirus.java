/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 21, 2014, 4:46:17 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemVirus extends ItemMod {

	private static final int SUBTYPES = 2;

	public ItemVirus() {
		super(LibItemNames.VIRUS);
		setHasSubtypes(true);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase, EnumHand hand) {
		if(par3EntityLivingBase instanceof EntityHorse) {
			EntityHorse horse = (EntityHorse) par3EntityLivingBase;
			if(horse.getType() != HorseArmorType.ZOMBIE && horse.getType() != HorseArmorType.SKELETON && horse.isTame()) {
				horse.dropChestItems();
				horse.setType(par1ItemStack.getItemDamage() == 0 ? HorseArmorType.ZOMBIE : HorseArmorType.SKELETON);
				AbstractAttributeMap attributes = horse.getAttributeMap();
				IAttributeInstance movementSpeed = attributes.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);
				IAttributeInstance health = attributes.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
				health.applyModifier(new AttributeModifier("Ermergerd Virus D:", health.getBaseValue(), 0));
				movementSpeed.applyModifier(new AttributeModifier("Ermergerd Virus D:", movementSpeed.getBaseValue(), 0));
				IAttributeInstance jumpHeight = attributes.getAttributeInstance(ReflectionHelper.<IAttribute, EntityHorse>getPrivateValue(EntityHorse.class, null, LibObfuscation.HORSE_JUMP_STRENGTH));
				jumpHeight.applyModifier(new AttributeModifier("Ermergerd Virus D:", jumpHeight.getBaseValue() * 0.5, 0));
				par3EntityLivingBase.playSound(SoundEvents.entity_zombie_villager_cure, 1.0F + par3EntityLivingBase.worldObj.rand.nextFloat(), par3EntityLivingBase.worldObj.rand.nextFloat() * 0.7F + 1.3F);

				par1ItemStack.stackSize--;
				return true;
			}
		}
		return false;
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if(entity.isRiding() && entity.getRidingEntity() instanceof EntityLivingBase)
			entity = (EntityLivingBase) entity.getRidingEntity();

		if(entity instanceof EntityHorse && event.getSource() == DamageSource.fall) {
			EntityHorse horse = (EntityHorse) entity;
			if((horse.getType() == HorseArmorType.ZOMBIE || horse.getType() == HorseArmorType.SKELETON) && horse.isTame())
				event.setCanceled(true);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		for(int i = 0; i < SUBTYPES; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return getUnlocalizedNameLazy(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	String getUnlocalizedNameLazy(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack);
	}

}
