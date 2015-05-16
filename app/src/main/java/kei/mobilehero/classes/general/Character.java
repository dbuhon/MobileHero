package kei.mobilehero.classes.general;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import kei.mobilehero.classes.attributes.Caracteristic;
import kei.mobilehero.classes.attributes.Effect;
import kei.mobilehero.classes.attributes.Equipment;
import kei.mobilehero.classes.attributes.Skill;

/**
 * Created by Dimitri on 15/05/2015.
 */
public class Character implements Serializable{
    private String id;
    private String name;
    private String gender;
    private String alignment;
    private String race;
    private String className;
    private String picture;
    private List<Skill> skills;
    private List<Caracteristic> caracteristics;
    private List<Equipment> equipments;

    public Character(String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        skills = new ArrayList<>();
        caracteristics = new ArrayList<>();
        equipments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Caracteristic> getCaracteristics() {
        return caracteristics;
    }

    public void setCaracteristics(List<Caracteristic> caracteristics) {
        this.caracteristics = caracteristics;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public HashMap<String, Double> getCalculatedCaracteristics(){
        HashMap<String, Double> calculatedCaracteristics = new HashMap<>();
        for (Caracteristic c : caracteristics){
            calculatedCaracteristics.put(c.getName(), c.getValue());
        }

        for (Equipment e : equipments){
            if (!e.getEffects().isEmpty()){
                for (Effect i : e.getEffects()){
                    Double value;
                    if ((value = calculatedCaracteristics.get(i.getCaracteristic().getName())) != null){
                        calculatedCaracteristics.put(i.getCaracteristic().getName(), value+i.getValue());
                    }
                }
            }
        }
        return calculatedCaracteristics;
    }

    public double getEquipmentWeight(){
        double sum = 0;
        for (Equipment e : equipments){
            sum += e.getWeight();
        }
        return sum;
    }

    public boolean save(Context context, String gameName, String roundName){

        FileOutputStream outputStream;
        ObjectOutputStream objectOutputStream;

        File characterFile = new File(context.getFilesDir(), gameName + "/" + roundName + "/character." + name + "." + id + ".hero");

        try {
            // Open the writer
            outputStream = new FileOutputStream(characterFile);
            objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(this);

            // Close the stream
            objectOutputStream.close();

            Log.v("Character save()", "Character saved in " + characterFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Context context, String gameName, String roundName){
        File file = new File (context.getFilesDir() + gameName+"/"+roundName+"/character."+name+"."+id+".hero");
        if (file.exists()){
            if (file.delete()) return true;
        }
        else Log.v("Character delete():", "Directory doesn't exist.");
        return false;
    }
}
