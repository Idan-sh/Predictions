//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.13 at 02:00:52 PM IDT 
//


package com.idansh.jaxb.schema.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice minOccurs="0">
 *         &lt;element ref="{}PRD-divide"/>
 *         &lt;element ref="{}PRD-multiply"/>
 *         &lt;sequence>
 *           &lt;element ref="{}PRD-condition"/>
 *           &lt;element ref="{}PRD-then"/>
 *           &lt;element ref="{}PRD-else" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/choice>
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="calculation"/>
 *             &lt;enumeration value="condition"/>
 *             &lt;enumeration value="decrease"/>
 *             &lt;enumeration value="increase"/>
 *             &lt;enumeration value="kill"/>
 *             &lt;enumeration value="set"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="result-prop" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="property" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="entity" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="by" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdDivide",
    "prdMultiply",
    "prdCondition",
    "prdThen",
    "prdElse"
})
@XmlRootElement(name = "PRD-action")
public class PRDAction {

    @XmlElement(name = "PRD-divide")
    protected PRDDivide prdDivide;
    @XmlElement(name = "PRD-multiply")
    protected PRDMultiply prdMultiply;
    @XmlElement(name = "PRD-condition")
    protected PRDCondition prdCondition;
    @XmlElement(name = "PRD-then")
    protected PRDThen prdThen;
    @XmlElement(name = "PRD-else")
    protected PRDElse prdElse;
    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "type", required = true)
    protected String type;
    @XmlAttribute(name = "result-prop")
    protected String resultProp;
    @XmlAttribute(name = "property")
    protected String property;
    @XmlAttribute(name = "entity", required = true)
    protected String entity;
    @XmlAttribute(name = "by")
    protected String by;

    /**
     * Gets the value of the prdDivide property.
     * 
     * @return
     *     possible object is
     *     {@link PRDDivide }
     *     
     */
    public PRDDivide getPRDDivide() {
        return prdDivide;
    }

    /**
     * Sets the value of the prdDivide property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRDDivide }
     *     
     */
    public void setPRDDivide(PRDDivide value) {
        this.prdDivide = value;
    }

    /**
     * Gets the value of the prdMultiply property.
     * 
     * @return
     *     possible object is
     *     {@link PRDMultiply }
     *     
     */
    public PRDMultiply getPRDMultiply() {
        return prdMultiply;
    }

    /**
     * Sets the value of the prdMultiply property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRDMultiply }
     *     
     */
    public void setPRDMultiply(PRDMultiply value) {
        this.prdMultiply = value;
    }

    /**
     * Gets the value of the prdCondition property.
     * 
     * @return
     *     possible object is
     *     {@link PRDCondition }
     *     
     */
    public PRDCondition getPRDCondition() {
        return prdCondition;
    }

    /**
     * Sets the value of the prdCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRDCondition }
     *     
     */
    public void setPRDCondition(PRDCondition value) {
        this.prdCondition = value;
    }

    /**
     * Gets the value of the prdThen property.
     * 
     * @return
     *     possible object is
     *     {@link PRDThen }
     *     
     */
    public PRDThen getPRDThen() {
        return prdThen;
    }

    /**
     * Sets the value of the prdThen property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRDThen }
     *     
     */
    public void setPRDThen(PRDThen value) {
        this.prdThen = value;
    }

    /**
     * Gets the value of the prdElse property.
     * 
     * @return
     *     possible object is
     *     {@link PRDElse }
     *     
     */
    public PRDElse getPRDElse() {
        return prdElse;
    }

    /**
     * Sets the value of the prdElse property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRDElse }
     *     
     */
    public void setPRDElse(PRDElse value) {
        this.prdElse = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the resultProp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultProp() {
        return resultProp;
    }

    /**
     * Sets the value of the resultProp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultProp(String value) {
        this.resultProp = value;
    }

    /**
     * Gets the value of the property property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets the value of the property property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProperty(String value) {
        this.property = value;
    }

    /**
     * Gets the value of the entity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Sets the value of the entity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntity(String value) {
        this.entity = value;
    }

    /**
     * Gets the value of the by property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBy() {
        return by;
    }

    /**
     * Sets the value of the by property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBy(String value) {
        this.by = value;
    }

}
