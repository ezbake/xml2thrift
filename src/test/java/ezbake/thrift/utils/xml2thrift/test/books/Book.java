/*   Copyright (C) 2013-2014 Computer Sciences Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package ezbake.thrift.utils.xml2thrift.test.books;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Book implements org.apache.thrift.TBase<Book, Book._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Book");

  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField NUMBER_FIELD_DESC = new org.apache.thrift.protocol.TField("number", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField AUTHOR_FIELD_DESC = new org.apache.thrift.protocol.TField("author", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField TITLE_FIELD_DESC = new org.apache.thrift.protocol.TField("title", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField GENRE_FIELD_DESC = new org.apache.thrift.protocol.TField("genre", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField PRICE_FIELD_DESC = new org.apache.thrift.protocol.TField("price", org.apache.thrift.protocol.TType.STRING, (short)6);
  private static final org.apache.thrift.protocol.TField PUBLISH_DATE_FIELD_DESC = new org.apache.thrift.protocol.TField("publish_date", org.apache.thrift.protocol.TType.STRING, (short)7);
  private static final org.apache.thrift.protocol.TField DESCRIPTION_FIELD_DESC = new org.apache.thrift.protocol.TField("description", org.apache.thrift.protocol.TType.STRING, (short)8);

  public String id;
  public String number;
  public String author;
  public String title;
  public String genre;
  public String price;
  public String publish_date;
  public String description;

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ID((short)1, "id"),
    NUMBER((short)2, "number"),
    AUTHOR((short)3, "author"),
    TITLE((short)4, "title"),
    GENRE((short)5, "genre"),
    PRICE((short)6, "price"),
    PUBLISH_DATE((short)7, "publish_date"),
    DESCRIPTION((short)8, "description");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ID
          return ID;
        case 2: // NUMBER
          return NUMBER;
        case 3: // AUTHOR
          return AUTHOR;
        case 4: // TITLE
          return TITLE;
        case 5: // GENRE
          return GENRE;
        case 6: // PRICE
          return PRICE;
        case 7: // PUBLISH_DATE
          return PUBLISH_DATE;
        case 8: // DESCRIPTION
          return DESCRIPTION;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments

  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.NUMBER, new org.apache.thrift.meta_data.FieldMetaData("number", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.AUTHOR, new org.apache.thrift.meta_data.FieldMetaData("author", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TITLE, new org.apache.thrift.meta_data.FieldMetaData("title", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.GENRE, new org.apache.thrift.meta_data.FieldMetaData("genre", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PRICE, new org.apache.thrift.meta_data.FieldMetaData("price", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PUBLISH_DATE, new org.apache.thrift.meta_data.FieldMetaData("publish_date", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.DESCRIPTION, new org.apache.thrift.meta_data.FieldMetaData("description", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Book.class, metaDataMap);
  }

  public Book() {
  }

  public Book(
    String id,
    String number,
    String author,
    String title,
    String genre,
    String price,
    String publish_date,
    String description)
  {
    this();
    this.id = id;
    this.number = number;
    this.author = author;
    this.title = title;
    this.genre = genre;
    this.price = price;
    this.publish_date = publish_date;
    this.description = description;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Book(Book other) {
    if (other.isSetId()) {
      this.id = other.id;
    }
    if (other.isSetNumber()) {
      this.number = other.number;
    }
    if (other.isSetAuthor()) {
      this.author = other.author;
    }
    if (other.isSetTitle()) {
      this.title = other.title;
    }
    if (other.isSetGenre()) {
      this.genre = other.genre;
    }
    if (other.isSetPrice()) {
      this.price = other.price;
    }
    if (other.isSetPublish_date()) {
      this.publish_date = other.publish_date;
    }
    if (other.isSetDescription()) {
      this.description = other.description;
    }
  }

  public Book deepCopy() {
    return new Book(this);
  }

  @Override
  public void clear() {
    this.id = null;
    this.number = null;
    this.author = null;
    this.title = null;
    this.genre = null;
    this.price = null;
    this.publish_date = null;
    this.description = null;
  }

  public String getId() {
    return this.id;
  }

  public Book setId(String id) {
    this.id = id;
    return this;
  }

  public void unsetId() {
    this.id = null;
  }

  /** Returns true if field id is set (has been assigned a value) and false otherwise */
  public boolean isSetId() {
    return this.id != null;
  }

  public void setIdIsSet(boolean value) {
    if (!value) {
      this.id = null;
    }
  }

  public String getNumber() {
    return this.number;
  }

  public Book setNumber(String number) {
    this.number = number;
    return this;
  }

  public void unsetNumber() {
    this.number = null;
  }

  /** Returns true if field number is set (has been assigned a value) and false otherwise */
  public boolean isSetNumber() {
    return this.number != null;
  }

  public void setNumberIsSet(boolean value) {
    if (!value) {
      this.number = null;
    }
  }

  public String getAuthor() {
    return this.author;
  }

  public Book setAuthor(String author) {
    this.author = author;
    return this;
  }

  public void unsetAuthor() {
    this.author = null;
  }

  /** Returns true if field author is set (has been assigned a value) and false otherwise */
  public boolean isSetAuthor() {
    return this.author != null;
  }

  public void setAuthorIsSet(boolean value) {
    if (!value) {
      this.author = null;
    }
  }

  public String getTitle() {
    return this.title;
  }

  public Book setTitle(String title) {
    this.title = title;
    return this;
  }

  public void unsetTitle() {
    this.title = null;
  }

  /** Returns true if field title is set (has been assigned a value) and false otherwise */
  public boolean isSetTitle() {
    return this.title != null;
  }

  public void setTitleIsSet(boolean value) {
    if (!value) {
      this.title = null;
    }
  }

  public String getGenre() {
    return this.genre;
  }

  public Book setGenre(String genre) {
    this.genre = genre;
    return this;
  }

  public void unsetGenre() {
    this.genre = null;
  }

  /** Returns true if field genre is set (has been assigned a value) and false otherwise */
  public boolean isSetGenre() {
    return this.genre != null;
  }

  public void setGenreIsSet(boolean value) {
    if (!value) {
      this.genre = null;
    }
  }

  public String getPrice() {
    return this.price;
  }

  public Book setPrice(String price) {
    this.price = price;
    return this;
  }

  public void unsetPrice() {
    this.price = null;
  }

  /** Returns true if field price is set (has been assigned a value) and false otherwise */
  public boolean isSetPrice() {
    return this.price != null;
  }

  public void setPriceIsSet(boolean value) {
    if (!value) {
      this.price = null;
    }
  }

  public String getPublish_date() {
    return this.publish_date;
  }

  public Book setPublish_date(String publish_date) {
    this.publish_date = publish_date;
    return this;
  }

  public void unsetPublish_date() {
    this.publish_date = null;
  }

  /** Returns true if field publish_date is set (has been assigned a value) and false otherwise */
  public boolean isSetPublish_date() {
    return this.publish_date != null;
  }

  public void setPublish_dateIsSet(boolean value) {
    if (!value) {
      this.publish_date = null;
    }
  }

  public String getDescription() {
    return this.description;
  }

  public Book setDescription(String description) {
    this.description = description;
    return this;
  }

  public void unsetDescription() {
    this.description = null;
  }

  /** Returns true if field description is set (has been assigned a value) and false otherwise */
  public boolean isSetDescription() {
    return this.description != null;
  }

  public void setDescriptionIsSet(boolean value) {
    if (!value) {
      this.description = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case ID:
      if (value == null) {
        unsetId();
      } else {
        setId((String)value);
      }
      break;

    case NUMBER:
      if (value == null) {
        unsetNumber();
      } else {
        setNumber((String)value);
      }
      break;

    case AUTHOR:
      if (value == null) {
        unsetAuthor();
      } else {
        setAuthor((String)value);
      }
      break;

    case TITLE:
      if (value == null) {
        unsetTitle();
      } else {
        setTitle((String)value);
      }
      break;

    case GENRE:
      if (value == null) {
        unsetGenre();
      } else {
        setGenre((String)value);
      }
      break;

    case PRICE:
      if (value == null) {
        unsetPrice();
      } else {
        setPrice((String)value);
      }
      break;

    case PUBLISH_DATE:
      if (value == null) {
        unsetPublish_date();
      } else {
        setPublish_date((String)value);
      }
      break;

    case DESCRIPTION:
      if (value == null) {
        unsetDescription();
      } else {
        setDescription((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ID:
      return getId();

    case NUMBER:
      return getNumber();

    case AUTHOR:
      return getAuthor();

    case TITLE:
      return getTitle();

    case GENRE:
      return getGenre();

    case PRICE:
      return getPrice();

    case PUBLISH_DATE:
      return getPublish_date();

    case DESCRIPTION:
      return getDescription();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case ID:
      return isSetId();
    case NUMBER:
      return isSetNumber();
    case AUTHOR:
      return isSetAuthor();
    case TITLE:
      return isSetTitle();
    case GENRE:
      return isSetGenre();
    case PRICE:
      return isSetPrice();
    case PUBLISH_DATE:
      return isSetPublish_date();
    case DESCRIPTION:
      return isSetDescription();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Book)
      return this.equals((Book)that);
    return false;
  }

  public boolean equals(Book that) {
    if (that == null)
      return false;

    boolean this_present_id = true && this.isSetId();
    boolean that_present_id = true && that.isSetId();
    if (this_present_id || that_present_id) {
      if (!(this_present_id && that_present_id))
        return false;
      if (!this.id.equals(that.id))
        return false;
    }

    boolean this_present_number = true && this.isSetNumber();
    boolean that_present_number = true && that.isSetNumber();
    if (this_present_number || that_present_number) {
      if (!(this_present_number && that_present_number))
        return false;
      if (!this.number.equals(that.number))
        return false;
    }

    boolean this_present_author = true && this.isSetAuthor();
    boolean that_present_author = true && that.isSetAuthor();
    if (this_present_author || that_present_author) {
      if (!(this_present_author && that_present_author))
        return false;
      if (!this.author.equals(that.author))
        return false;
    }

    boolean this_present_title = true && this.isSetTitle();
    boolean that_present_title = true && that.isSetTitle();
    if (this_present_title || that_present_title) {
      if (!(this_present_title && that_present_title))
        return false;
      if (!this.title.equals(that.title))
        return false;
    }

    boolean this_present_genre = true && this.isSetGenre();
    boolean that_present_genre = true && that.isSetGenre();
    if (this_present_genre || that_present_genre) {
      if (!(this_present_genre && that_present_genre))
        return false;
      if (!this.genre.equals(that.genre))
        return false;
    }

    boolean this_present_price = true && this.isSetPrice();
    boolean that_present_price = true && that.isSetPrice();
    if (this_present_price || that_present_price) {
      if (!(this_present_price && that_present_price))
        return false;
      if (!this.price.equals(that.price))
        return false;
    }

    boolean this_present_publish_date = true && this.isSetPublish_date();
    boolean that_present_publish_date = true && that.isSetPublish_date();
    if (this_present_publish_date || that_present_publish_date) {
      if (!(this_present_publish_date && that_present_publish_date))
        return false;
      if (!this.publish_date.equals(that.publish_date))
        return false;
    }

    boolean this_present_description = true && this.isSetDescription();
    boolean that_present_description = true && that.isSetDescription();
    if (this_present_description || that_present_description) {
      if (!(this_present_description && that_present_description))
        return false;
      if (!this.description.equals(that.description))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(Book other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Book typedOther = (Book)other;

    lastComparison = Boolean.valueOf(isSetId()).compareTo(typedOther.isSetId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.id, typedOther.id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetNumber()).compareTo(typedOther.isSetNumber());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNumber()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.number, typedOther.number);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAuthor()).compareTo(typedOther.isSetAuthor());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAuthor()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.author, typedOther.author);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTitle()).compareTo(typedOther.isSetTitle());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTitle()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.title, typedOther.title);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetGenre()).compareTo(typedOther.isSetGenre());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGenre()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.genre, typedOther.genre);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPrice()).compareTo(typedOther.isSetPrice());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPrice()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.price, typedOther.price);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPublish_date()).compareTo(typedOther.isSetPublish_date());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPublish_date()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.publish_date, typedOther.publish_date);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDescription()).compareTo(typedOther.isSetDescription());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDescription()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.description, typedOther.description);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    org.apache.thrift.protocol.TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == org.apache.thrift.protocol.TType.STOP) { 
        break;
      }
      switch (field.id) {
        case 1: // ID
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.id = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // NUMBER
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.number = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 3: // AUTHOR
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.author = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 4: // TITLE
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.title = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 5: // GENRE
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.genre = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 6: // PRICE
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.price = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 7: // PUBLISH_DATE
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.publish_date = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 8: // DESCRIPTION
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.description = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();

    // check for required fields of primitive type, which can't be checked in the validate method
    validate();
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.id != null) {
      oprot.writeFieldBegin(ID_FIELD_DESC);
      oprot.writeString(this.id);
      oprot.writeFieldEnd();
    }
    if (this.number != null) {
      oprot.writeFieldBegin(NUMBER_FIELD_DESC);
      oprot.writeString(this.number);
      oprot.writeFieldEnd();
    }
    if (this.author != null) {
      oprot.writeFieldBegin(AUTHOR_FIELD_DESC);
      oprot.writeString(this.author);
      oprot.writeFieldEnd();
    }
    if (this.title != null) {
      oprot.writeFieldBegin(TITLE_FIELD_DESC);
      oprot.writeString(this.title);
      oprot.writeFieldEnd();
    }
    if (this.genre != null) {
      oprot.writeFieldBegin(GENRE_FIELD_DESC);
      oprot.writeString(this.genre);
      oprot.writeFieldEnd();
    }
    if (this.price != null) {
      oprot.writeFieldBegin(PRICE_FIELD_DESC);
      oprot.writeString(this.price);
      oprot.writeFieldEnd();
    }
    if (this.publish_date != null) {
      oprot.writeFieldBegin(PUBLISH_DATE_FIELD_DESC);
      oprot.writeString(this.publish_date);
      oprot.writeFieldEnd();
    }
    if (this.description != null) {
      oprot.writeFieldBegin(DESCRIPTION_FIELD_DESC);
      oprot.writeString(this.description);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Book(");
    boolean first = true;

    sb.append("id:");
    if (this.id == null) {
      sb.append("null");
    } else {
      sb.append(this.id);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("number:");
    if (this.number == null) {
      sb.append("null");
    } else {
      sb.append(this.number);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("author:");
    if (this.author == null) {
      sb.append("null");
    } else {
      sb.append(this.author);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("title:");
    if (this.title == null) {
      sb.append("null");
    } else {
      sb.append(this.title);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("genre:");
    if (this.genre == null) {
      sb.append("null");
    } else {
      sb.append(this.genre);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("price:");
    if (this.price == null) {
      sb.append("null");
    } else {
      sb.append(this.price);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("publish_date:");
    if (this.publish_date == null) {
      sb.append("null");
    } else {
      sb.append(this.publish_date);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("description:");
    if (this.description == null) {
      sb.append("null");
    } else {
      sb.append(this.description);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

}

