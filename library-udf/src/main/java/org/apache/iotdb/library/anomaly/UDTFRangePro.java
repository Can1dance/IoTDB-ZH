/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.iotdb.library.anomaly;

import org.apache.iotdb.commons.udf.utils.UDFDataTypeTransformer;
import org.apache.iotdb.library.util.Util;
import org.apache.iotdb.tsfile.file.metadata.enums.TSDataType;
import org.apache.iotdb.udf.api.UDTF;
import org.apache.iotdb.udf.api.access.Row;
import org.apache.iotdb.udf.api.collector.PointCollector;
import org.apache.iotdb.udf.api.customizer.config.UDTFConfigurations;
import org.apache.iotdb.udf.api.customizer.parameter.UDFParameterValidator;
import org.apache.iotdb.udf.api.customizer.parameter.UDFParameters;
import org.apache.iotdb.udf.api.customizer.strategy.RowByRowAccessStrategy;
import org.apache.iotdb.udf.api.type.Type;

/** This function is used to detect range anomaly of time series. */
public class UDTFRangePro implements UDTF {
  private TSDataType dataType;
  private String abnormal_level;
  private String abnormal_name;
  private String[] abnormal_level_arr;
  private String[] abnormal_name_arr;

  @Override
  public void validate(UDFParameterValidator validator) throws Exception {
    validator
        .validateInputSeriesNumber(1)
        .validateInputSeriesDataType(0, Type.INT32, Type.INT64, Type.FLOAT, Type.DOUBLE);
  }

  @Override
  public void beforeStart(UDFParameters parameters, UDTFConfigurations configurations)
      throws Exception {
    configurations.setAccessStrategy(new RowByRowAccessStrategy()).setOutputDataType(Type.TEXT);
    //    ?????????????????????????????????
    this.abnormal_level = parameters.getString("abnormal_level");
    //    ?????????????????????
    this.abnormal_name = parameters.getString("abnormal_name");
    abnormal_level_arr = abnormal_level.split(",");
    abnormal_name_arr = abnormal_name.split(",");

    //    ???????????????????????????
    this.dataType = UDFDataTypeTransformer.transformToTsDataType(parameters.getDataType(0));
  }

  @Override
  public void transform(Row row, PointCollector collector) throws Exception {
    int intValue;
    long longValue;
    float floatValue;
    double doubleValue;
    long timestamp;
    int count = 0;
    timestamp = row.getTime();
    switch (dataType) {
      case INT32:
        intValue = row.getInt(0);
        for (int i = 0; i < abnormal_level_arr.length; i++) {
          if (intValue < Double.valueOf(abnormal_level_arr[i])) {
            break;
          }
          count++;
        }
        if (count == abnormal_level_arr.length) {
          count = 0;
        }
        if ((count != 0 && !abnormal_name_arr[count - 1].equalsIgnoreCase("normal"))) {
          try {
            Util.putValue(
                collector,
                TSDataType.TEXT,
                timestamp,
                "LEVEL:" + abnormal_name_arr[count - 1] + " VALUE:" + intValue);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("??????????????????????????????????????????????????????????????????????????????");
          }
        }
        break;
      case INT64:
        longValue = row.getLong(0);
        for (int i = 0; i < abnormal_level_arr.length; i++) {
          if (longValue < Double.valueOf(abnormal_level_arr[i])) {
            break;
          }
          count++;
        }
        if (count == abnormal_level_arr.length) {
          count = 0;
        }
        if ((count != 0 && !abnormal_name_arr[count - 1].equalsIgnoreCase("normal"))) {
          try {
            Util.putValue(
                collector,
                TSDataType.TEXT,
                timestamp,
                "LEVEL:" + abnormal_name_arr[count - 1] + " VALUE:" + longValue);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("??????????????????????????????????????????????????????????????????????????????");
          }
        }
        break;
      case FLOAT:
        floatValue = row.getFloat(0);
        for (int i = 0; i < abnormal_level_arr.length; i++) {
          if (floatValue < Double.valueOf(abnormal_level_arr[i])) {
            break;
          }
          count++;
        }
        if (count == abnormal_level_arr.length) {
          count = 0;
        }
        if ((count != 0 && !abnormal_name_arr[count - 1].equalsIgnoreCase("normal"))) {
          try {
            Util.putValue(
                collector,
                TSDataType.TEXT,
                timestamp,
                "LEVEL:" + abnormal_name_arr[count - 1] + " VALUE:" + floatValue);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("??????????????????????????????????????????????????????????????????????????????");
          }
        }
        break;
      case DOUBLE:
        doubleValue = row.getDouble(0);
        for (int i = 0; i < abnormal_level_arr.length; i++) {
          if (doubleValue < Double.valueOf(abnormal_level_arr[i])) {
            break;
          }
          count++;
        }
        if (count == abnormal_level_arr.length) {
          count = 0;
        }
        if ((count != 0 && !abnormal_name_arr[count - 1].equalsIgnoreCase("normal"))) {
          try {
            Util.putValue(
                collector,
                TSDataType.TEXT,
                timestamp,
                "LEVEL:" + abnormal_name_arr[count - 1] + " VALUE:" + doubleValue);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("??????????????????????????????????????????????????????????????????????????????");
          }
        }
        break;
      default:
        throw new Exception("No such kind of data type.");
    }
  }

  @Override
  public void terminate(PointCollector collector) throws Exception {}
}
