package org.campagnelab.mercury.api;


import java.util.List;
import java.util.Map;

import static org.campagnelab.gobyweb.mercury.test.protos.FileSetMetadata.*;


/**
 * Create a protocol buffer with metadata describing a fileset instance.
 *
 * @author manuele
 */
public class FileSetMetadataWriter {

    public static final String PB_FILENAME = "metadata.pb";
    public static final String BASENAME_ATTRIBUTE_NAME = "BASENAME";
    public static final String GENERATED_BY_ATTRIBUTE_NAME = "GENERATED_BY";


    private Metadata.Builder metadataBuilder;

    public FileSetMetadataWriter(String name, String version, String tag, String owner) {
        metadataBuilder = Metadata.newBuilder();
        metadataBuilder.setName(name);
        metadataBuilder.setTag(tag);
        metadataBuilder.setOwner(owner);
        metadataBuilder.setVersion(version);
    }

    /**
     * Adds a new entry.
     * @param name
     * @param path
     * @param size
     */
    public void addEntry(String name, String path, long size) {
        Metadata.Entry.Builder entryBuilder = Metadata.Entry.newBuilder();
        entryBuilder.setName(name);
        entryBuilder.setPath(path);
        entryBuilder.setSize(size);
        metadataBuilder.addEntry(entryBuilder.build());
    }

    /**
     * Sets the source of the fileset.
     * @param generatedBy
     */
    public void setGeneratedBy(String generatedBy) {
        Metadata.Attribute.Builder attributeBuilder = Metadata.Attribute.newBuilder();
        attributeBuilder.setName(GENERATED_BY_ATTRIBUTE_NAME);
        attributeBuilder.setValue(generatedBy);
        metadataBuilder.addAttribute(attributeBuilder.build());
    }

    /**
     * Sets the basename.
     * @param basename
     */
    public void setBasename(String basename) {
        Metadata.Attribute.Builder attributeBuilder = Metadata.Attribute.newBuilder();
        attributeBuilder.setName(BASENAME_ATTRIBUTE_NAME);
        attributeBuilder.setValue(basename);
        metadataBuilder.addAttribute(attributeBuilder.build());
    }

    /**
     * Adds the attributes to the metadata.
     * @param attributes
     */
    public void addAttributes(Map<String, String> attributes) {
        for (Map.Entry<String,String> attribute : attributes.entrySet())
            this.addAttribute(attribute.getKey(),attribute.getValue());
    }
    /**
     * Adds the attribute to the metadata.
     * @param name
     * @param value
     */
    public void addAttribute(String name, String value) {
        this.removeAttribute(name);//firstly, remove the attribute if it exists
        Metadata.Attribute.Builder attributeBuilder = Metadata.Attribute.newBuilder();
        attributeBuilder.setName(name);
        attributeBuilder.setValue(value);
        metadataBuilder.addAttribute(attributeBuilder.build());
    }

    /**
     * Removes the attribute from the metadata.
     * @param name
     */
    public void removeAttribute(String name) {
        List<Metadata.Attribute> attributes = metadataBuilder.getAttributeList();
        int toRemove = 0;
        for (Metadata.Attribute storedAttribute : attributes) {
            if (storedAttribute.getName().equalsIgnoreCase(name)) {
                metadataBuilder.removeAttribute(toRemove);
                return;
            } else
                toRemove++;
        }
    }

    /**
     * Adds a new user to the shared list.
     * @param user
     */
    public void addSharedWith(String user) {
        metadataBuilder.addSharedWith(user);
    }

    /**
     * Removes a user from the shared list.
     * @param user
     */
    public void removeSharedWith(String user) {
        metadataBuilder.getSharedWithList().remove(user);
    }

    public Metadata serialize() {
        return this.metadataBuilder.build();
    }

}
