package com.tuyano.springboot;

import java.util.*;

public class SampleProductDao {

	public List<SampleProductModel> findByAll(){
        List<SampleProductModel> result = new ArrayList<>();

        SampleProductModel data1 = new SampleProductModel("アボカド",200);
        SampleProductModel data2 = new SampleProductModel("オレンジ",250);

        result.add(data1);
        result.add(data2);

        return result;
	}
}
