package fr.eni.dataminer.service.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serialiseur<T> {
	
	 private String fileName;
	 private T object;
	
	/**
	 * Public method to serialize an object in .dat file
	 * @param obj
	 * @throws IOException
	 */
	public void serialize(Object obj) throws IOException{
		
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		
		fos = new FileOutputStream(fileName);
		oos = new ObjectOutputStream(fos);
		try {
			oos.writeObject(obj); 
			oos.flush();
		} finally {
			oos.close();
			fos.close();
		}	
	}
	
	/**
	 * Public method to deserialize an object from .dat file
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public T deserialize() throws IOException{
		
		T data = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try {
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			
			data = (T) ois.readObject(); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			ois.close();
			fis.close();
		}
		return data;
	}
		
	public String getFileName() {
			return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T objectType) {
		this.object = objectType;
	}

	public Serialiseur(String fileName, T object) {
		  this.fileName = fileName;
		  this.object = object;
	 }
}
