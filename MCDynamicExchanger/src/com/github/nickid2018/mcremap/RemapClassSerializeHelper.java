package com.github.nickid2018.mcremap;

import java.io.*;
import java.util.*;
import com.github.nickid2018.util.*;

public class RemapClassSerializeHelper {

	private static RemapClassSerializeHelper instance;

	public static RemapClassSerializeHelper getInstance() {
		return instance == null ? (instance = new RemapClassSerializeHelper()) : instance;
	}

	public Map<String, String[]> inheritTree = new HashMap<>();
	public Map<String, RemapClass> classes;
	public String md5;
	public boolean reverse;

	public void doSerialize(Map<String, RemapClass> classes, String path, InputStream mf) throws IOException {
		reverse = false;
		this.classes = classes;
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		oos.writeUTF(SHACompute.getSHA256(mf));
		oos.writeObject(classes);
		oos.writeObject(inheritTree);
		oos.close();
	}

	public void doReverseSerialize(Map<String, RemapClass> classes, String path, InputStream mf) throws IOException {
		reverse = true;
		this.classes = classes;
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		oos.writeUTF(SHACompute.getSHA256(mf));
		oos.writeObject(reverseMap());
		oos.writeObject(inheritTree);
		oos.close();
	}

	private Map<String, RemapClass> reverseMap() {
		Map<String, RemapClass> reverse = new HashMap<>();
		classes.forEach((name, clazz) -> {
			reverse.put(clazz.remapName, clazz);
		});
		return reverse;
	}

	public String tryMD5(String path) throws IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		md5 = ois.readUTF();
		ois.close();
		return md5;
	}

	@SuppressWarnings("unchecked")
	public void doDeserialize(String path) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		md5 = ois.readUTF();
		classes = (Map<String, RemapClass>) ois.readObject();
		inheritTree = (Map<String, String[]>) ois.readObject();
		ois.close();
		for (Map.Entry<String, String[]> en : inheritTree.entrySet()) {
			RemapClass clazz = classes.get(en.getKey());
			for (String sup : en.getValue())
				clazz.superClasses.add(classes.get(sup));
		}
	}
}
