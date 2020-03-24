/**
 * 5G-EVE TSM module
 * Ticketing system module for 5G-EVE portal
 *
 * OpenAPI spec version: 0.0.1
 * Contact: gigarcia@it.uc3m.es
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.swagger.client.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * NewTrustedTicket
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2020-03-17T14:22:48.055+01:00")
public class NewTrustedTicket   {
  @SerializedName("reporter")
  private String reporter = null;

  @SerializedName("product")
  private String product = null;

  @SerializedName("component")
  private String component = null;

  @SerializedName("summary")
  private String summary = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("assigned_to")
  private String assignedTo = null;

  public NewTrustedTicket reporter(String reporter) {
    this.reporter = reporter;
    return this;
  }

   /**
   * email of the user creating the ticket
   * @return reporter
  **/
  @ApiModelProperty(example = "null", value = "email of the user creating the ticket")
  public String getReporter() {
    return reporter;
  }

  public void setReporter(String reporter) {
    this.reporter = reporter;
  }

  public NewTrustedTicket product(String product) {
    this.product = product;
    return this;
  }

   /**
   * Name of the product where the ticket will be placed
   * @return product
  **/
  @ApiModelProperty(example = "null", value = "Name of the product where the ticket will be placed")
  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public NewTrustedTicket component(String component) {
    this.component = component;
    return this;
  }

   /**
   * Name of the component where the ticket will be placed
   * @return component
  **/
  @ApiModelProperty(example = "null", value = "Name of the component where the ticket will be placed")
  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public NewTrustedTicket summary(String summary) {
    this.summary = summary;
    return this;
  }

   /**
   * Get summary
   * @return summary
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public NewTrustedTicket description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public NewTrustedTicket assignedTo(String assignedTo) {
    this.assignedTo = assignedTo;
    return this;
  }

   /**
   * Get assignedTo
   * @return assignedTo
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getAssignedTo() {
    return assignedTo;
  }

  public void setAssignedTo(String assignedTo) {
    this.assignedTo = assignedTo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewTrustedTicket newTrustedTicket = (NewTrustedTicket) o;
    return Objects.equals(this.reporter, newTrustedTicket.reporter) &&
        Objects.equals(this.product, newTrustedTicket.product) &&
        Objects.equals(this.component, newTrustedTicket.component) &&
        Objects.equals(this.summary, newTrustedTicket.summary) &&
        Objects.equals(this.description, newTrustedTicket.description) &&
        Objects.equals(this.assignedTo, newTrustedTicket.assignedTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reporter, product, component, summary, description, assignedTo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewTrustedTicket {\n");
    
    sb.append("    reporter: ").append(toIndentedString(reporter)).append("\n");
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    component: ").append(toIndentedString(component)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    assignedTo: ").append(toIndentedString(assignedTo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

