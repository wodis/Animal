package com.openwudi.animal.model;

import java.util.Set;

/**
 * Created by diwu on 17/7/18.
 */

public class UpObject {
    private Long id;
    private Animal animal;
    private DataAcquisition dataAcquisition;
    private Item qixidi;
    private Set<Item> zhuangtai;
    private Item juli;
    private Item fangwei;
    private Item weizhi;

    public UpObject(Long id, Animal animal, DataAcquisition dataAcquisition, Item qixidi, Set<Item> zhuangtai, Item juli, Item fangwei, Item weizhi) {
        this.id = id;
        this.animal = animal;
        this.dataAcquisition = dataAcquisition;
        this.qixidi = qixidi;
        this.zhuangtai = zhuangtai;
        this.juli = juli;
        this.fangwei = fangwei;
        this.weizhi = weizhi;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public DataAcquisition getDataAcquisition() {
        return dataAcquisition;
    }

    public void setDataAcquisition(DataAcquisition dataAcquisition) {
        this.dataAcquisition = dataAcquisition;
    }

    public Item getQixidi() {
        return qixidi;
    }

    public void setQixidi(Item qixidi) {
        this.qixidi = qixidi;
    }

    public Set<Item> getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(Set<Item> zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public Item getJuli() {
        return juli;
    }

    public void setJuli(Item juli) {
        this.juli = juli;
    }

    public Item getFangwei() {
        return fangwei;
    }

    public void setFangwei(Item fangwei) {
        this.fangwei = fangwei;
    }

    public Item getWeizhi() {
        return weizhi;
    }

    public void setWeizhi(Item weizhi) {
        this.weizhi = weizhi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
