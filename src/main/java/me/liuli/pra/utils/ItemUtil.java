package me.liuli.pra.utils;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;

public class ItemUtil {
    public static Item itemPhaser(String itemString){
        int count=1;
        if(itemString.contains("-")){
            String[] strData=itemString.split("-");
            count=new Integer(strData[1]);
            itemString=strData[0];
        }

        String[] itemData=itemString.split("\\(");
        String[] baseData=(itemData[0]).split(":");
        int meta=0;
        if(baseData.length>1){
            meta=new Integer(baseData[1]);
        }
        Item item=Item.get(new Integer(baseData[0]),meta,count);

        if(itemData.length>1){
            String[] enchants = itemData[1].substring(0,itemData[1].length()-1).split(",");
            for(String enchant:enchants){
                String[] enchantData=enchant.split(":");
                item.addEnchantment(Enchantment.getEnchantment(new Integer(enchantData[0])).setLevel(new Integer(enchantData[1])));
            }
        }

        return item;
    }
}
