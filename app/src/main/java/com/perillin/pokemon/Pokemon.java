package com.perillin.pokemon;

import android.graphics.Bitmap;

import java.util.List;

public class Pokemon {
    // 1 и 4. берём список покемонов, по урлу находим каждого https://pokeapi.co/api/v2/pokemon?limit=30&offset=0
    //подгружаем по 30, то есть, делаем запрос и меняем offset+=30
    //height
    //weight
    //sprites/front_default photo
    //stats/0-2/
    //          base_stat -значение параметра
    //          stat/name - название параметрa
    //type/0-n/slot
    //          type/
    //              name - название типа покемона
    private int weight;
    private int height;
    private int id;
    private int hp;
    private int defence;
    private int attack;
    private List<String> types;
    private String name;
    private String sprite;
    private Bitmap image;
    public Pokemon (int weight, int height, int id, int hp, int defence, int attack, List<String> types, String name, String sprite)
    {
        this.weight=weight;
        this.attack=attack;
        this.defence=defence;
        this.height=height;
        this.hp=hp;
        this.id=id;
        this.types=types;
        this.name=name;
        this.sprite=sprite;
    }


    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }

    public int getHp() {
        return hp;
    }

    public int getDefence() {
        return defence;
    }

    public int getAttack() {
        return attack;
    }

    public String getSprite() {
        return sprite;
    }

    public List<String> getTypes() {
        return types;
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
