package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daidaijie on 2016/9/28.
 */

public class TakeOutBuyBean {

    private int num;

    private int sumPrice;

    private int unCalcNum;

    private Map<Dishes, Integer> mBuyMap;

    private List<Dishes> mDishesList;


    public TakeOutBuyBean() {
        num = 0;
        sumPrice = 0;
        unCalcNum = 0;
        mBuyMap = new LinkedHashMap<>();
        mDishesList = new ArrayList<>();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Map<Dishes, Integer> getBuyMap() {
        return mBuyMap;
    }

    public void setBuyMap(Map<Dishes, Integer> buyMap) {
        mBuyMap = buyMap;
    }

    public void addDishes(Dishes dishes) {
        if (mBuyMap.get(dishes) != null) {
            mBuyMap.put(dishes, mBuyMap.get(dishes) + 1);
        } else {
            mBuyMap.put(dishes, 1);
            mDishesList.add(dishes);
        }
        num++;
        if (StringUtil.isNumberic(dishes.getPrice())) {
            sumPrice += Integer.parseInt(dishes.getPrice());
        } else {
            unCalcNum++;
        }
    }

    public boolean removeDishes(Dishes dishes) {
        boolean isNone = false;
        if (mBuyMap.get(dishes) != null) {
            mBuyMap.put(dishes, mBuyMap.get(dishes) - 1);
            if (mBuyMap.get(dishes) <= 0) {
                mBuyMap.remove(dishes);
                mDishesList.remove(dishes);
                isNone = true;
            }
            num--;
            if (StringUtil.isNumberic(dishes.getPrice())) {
                sumPrice -= Integer.parseInt(dishes.getPrice());
            } else {
                unCalcNum--;
            }
        } else {
            isNone = true;
        }
        return isNone;
    }

    public List<Dishes> getDishesList() {
        return mDishesList;
    }

    public void setDishesList(List<Dishes> dishesList) {
        mDishesList = dishesList;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(int sumPrice) {
        this.sumPrice = sumPrice;
    }

    public int getUnCalcNum() {
        return unCalcNum;
    }

    public void setUnCalcNum(int unCalcNum) {
        this.unCalcNum = unCalcNum;
    }

    public void clear(){
        num = 0;
        sumPrice = 0;
        unCalcNum = 0;
        mBuyMap.clear();
        mDishesList.clear();
    }
}