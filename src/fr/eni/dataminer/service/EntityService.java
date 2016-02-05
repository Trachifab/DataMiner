package fr.eni.dataminer.service;

import java.util.List;
import java.util.Map;

import fr.eni.dataminer.model.Entity;

public class EntityService {

	private static EntityService instance = new EntityService();

	private List<Entity> entityList = null;

	public static EntityService getInstance() {
		return instance;
	}

	public List<Entity> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<Entity> resultList) {
		this.entityList = resultList;
	}
	
	

}
