package fr.estecka.invarpaint.config;

import java.util.HashMap;
import java.util.Map;
import fr.estecka.invarpaint.config.ConfigIO.Property;

public class Config
extends ConfigIO.AFixedCoded
{
	public boolean setItemModel = true;

	@Override
	public Map<String, Property<?>> GetProperties(){
		return new HashMap<>(){{
			put("server.item_model", Property.Boolean(()->setItemModel, v->setItemModel=v));
		}};
	}
}
