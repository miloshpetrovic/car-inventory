package com.smg.carinventory.presentation.controller;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class EntityModelMapper {
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static  <T, K> EntityModel<K> entityModelOf(T t, Class<K> clazz, Link link, Object...objects){ 
		K a = modelMapper.map(t, clazz);
		var model = EntityModel.of(a);
		
		return model.add(link.expand(objects));
	}

	public static  <T, K> EntityModel<K> entityModelOf(T t, Class<K> clazz){ 
		K a = modelMapper.map(t, clazz);
		var model = EntityModel.of(a);
		
		return model;
	}

}
