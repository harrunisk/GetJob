package com.example.harun.getjob.JobSearch.JobUtils;

import com.example.harun.getjob.R;

import java.util.ArrayList;

/**
 * Created by mayne on 22.03.2018.
 */

public class CategoryModel {
    int[] res1 = {R.drawable.scientist,
            R.drawable.chef, R.drawable.businessman, R.drawable.doctor, R.drawable.manager, R.drawable.worker, R.drawable.workerman,
            R.drawable.scientist, R.drawable.scientist, R.drawable.scientist};

    private String categoryName;
    private int res;

    public CategoryModel() {
    }

    public CategoryModel(String categoryName, int res) {
        this.categoryName = categoryName;
        this.res = res;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }


    public ArrayList<CategoryModel> getDataModel() {
        ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            categoryModelArrayList.add(new CategoryModel("kategori " + i, res1[i]));

        }


        return categoryModelArrayList;
    }

}
