//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.10 at 09:54:04 PM IDT 
//


package com.idansh.jaxb.generated;

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
 *       &lt;sequence>
 *         &lt;element ref="{}PRD-name"/>
 *         &lt;element ref="{}PRD-range" minOccurs="0"/>
 *         &lt;element ref="{}PRD-value"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="boolean"/>
 *             &lt;enumeration value="decimal"/>
 *             &lt;enumeration value="float"/>
 *             &lt;enumeration value="string"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdName",
    "prdRange",
    "prdValue"
})
@XmlRootElement(name = "PRD-property")
public class PRDProperty {

    @XmlElement(name = "PRD-name", required = true)
    protected String prdName;
    @XmlElement(name = "PRD-range")
    protected PRDRange prdRange;
    @XmlElement(name = "PRD-value", required = true)
    protected PRDValue prdValue;
    @XmlAttribute(name = "type", required = true)
    protected String type;

    /**
     * Gets the value of the prdName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRDName() {
        return prdName;
    }

    /**
     * Sets the value of the prdName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRDName(String value) {
        this.prdName = value;
    }

    /**
     * Gets the value of the prdRange property.
     * 
     * @return
     *     possible object is
     *     {@link PRDRange }
     *     
     */
    public PRDRange getPRDRange() {
        return prdRange;
    }

    /**
     * Sets the value of the prdRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRDRange }
     *     
     */
    public void setPRDRange(PRDRange value) {
        this.prdRange = value;
    }

    /**
     * Gets the value of the prdValue property.
     * 
     * @return
     *     possible object is
     *     {@link PRDValue }
     *     
     */
    public PRDValue getPRDValue() {
        return prdValue;
    }

    /**
     * Sets the value of the prdValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRDValue }
     *     
     */
    public void setPRDValue(PRDValue value) {
        this.prdValue = value;
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

}
