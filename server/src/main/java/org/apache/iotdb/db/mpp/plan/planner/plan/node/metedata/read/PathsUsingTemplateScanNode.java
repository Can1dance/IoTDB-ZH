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

package org.apache.iotdb.db.mpp.plan.planner.plan.node.metedata.read;

import org.apache.iotdb.db.mpp.common.header.HeaderConstant;
import org.apache.iotdb.db.mpp.plan.planner.plan.node.PlanNode;
import org.apache.iotdb.db.mpp.plan.planner.plan.node.PlanNodeId;
import org.apache.iotdb.db.mpp.plan.planner.plan.node.PlanNodeType;
import org.apache.iotdb.tsfile.utils.ReadWriteIOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class PathsUsingTemplateScanNode extends SchemaQueryScanNode {

  private int templateId;

  public PathsUsingTemplateScanNode(PlanNodeId id, int templateId) {
    super(id);
    this.templateId = templateId;
  }

  public int getTemplateId() {
    return templateId;
  }

  @Override
  public PlanNode clone() {
    return new PathsUsingTemplateScanNode(getPlanNodeId(), templateId);
  }

  @Override
  public List<String> getOutputColumnNames() {
    return HeaderConstant.showPathsUsingTemplate.getRespColumns();
  }

  @Override
  protected void serializeAttributes(ByteBuffer byteBuffer) {
    PlanNodeType.PATHS_USING_TEMPLATE_SCAN.serialize(byteBuffer);

    ReadWriteIOUtils.write(templateId, byteBuffer);
  }

  @Override
  protected void serializeAttributes(DataOutputStream stream) throws IOException {
    PlanNodeType.PATHS_USING_TEMPLATE_SCAN.serialize(stream);

    ReadWriteIOUtils.write(templateId, stream);
  }

  public static PathsUsingTemplateScanNode deserialize(ByteBuffer buffer) {
    int templateId = ReadWriteIOUtils.readInt(buffer);
    PlanNodeId planNodeId = PlanNodeId.deserialize(buffer);
    return new PathsUsingTemplateScanNode(planNodeId, templateId);
  }
}
