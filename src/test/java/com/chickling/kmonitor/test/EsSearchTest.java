package com.chickling.kmonitor.test;

import java.util.List;

import com.chickling.kmonitor.model.OffsetPoints;
import com.chickling.kmonitor.utils.ElasticsearchUtil;

/**
 * @author Hulva Luva.H from ECBD
 * @date 2017年6月28日
 * @description
 *
 */
public class EsSearchTest {

  public static void main(String[] args) {
    ElasticsearchUtil es = new ElasticsearchUtil("", "10.16.238.82:9300,10.16.238.83:9300,10.16.238.84:9300");
    es.setIndexAndType("logx_healthcheck_test", "kafkaoffset");
    List<OffsetPoints> result = es.offsetHistory("testkafka", "EC2_Test");

    System.out.println(result);
  }

}
