/**
 *  Class : OntObject
 *  Date  : May 24, 2010
 *   .       .     ..
 *  _| _  _.*|_  _ ||
 * (_](/,(_.|[_)(/,||
 *
 * DeciBell : A Java Tool for creating and managing relational databases.
 *  DeciBell is a Object - Relation database mapper for java applications providing
 * an easy-to-use interface making it easy for the developer to build a relational
 * database and moreover perform database operations easily!
 *  This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Charalampos Chomenides & Pantelis Sopasakis
 *                         kinkyDesign ~ OpenSource Development

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * hampos Att yahoo d0t c0m
 * chvng att mail D0t ntua dd0T gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */
package org.kinkydesign.decibell.rdf.ns;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.impl.OntModelImpl;
import com.hp.hpl.jena.rdf.model.Model;
import java.io.InputStream;
import java.util.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OntObject extends OntModelImpl {

    private void setNamespacePrefices() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("db", OntEntity.DECIBELL_BASE);
        map.put("db_datatypes", OntEntity.DECIBELL_DATATYPES);
        this.setNsPrefixes(map);
    }

    public OntObject() {
        super(OntModelSpec.OWL_DL_MEM);
        setNamespacePrefices();
    }

    public OntObject(OntObject other) {
        super(OntModelSpec.OWL_DL_MEM, other);
        setNamespacePrefices();
    }

    public OntObject(Model other) {
        super(OntModelSpec.OWL_DL_MEM, other);
        setNamespacePrefices();
    }

    public OntObject(OntModelSpec spec) {
        super(spec);
        setNamespacePrefices();
    }

    public OntObject(InputStream in) {
        super(OntModelSpec.OWL_DL_MEM);
        setNamespacePrefices();
        read(in, null);
    }

    public void printConsole() {
        this.write(System.out);
    }

    public void createAnnotationProperties(String... annotation_uris) {
        if (annotation_uris != null) {
            for (int i = 0; i < annotation_uris.length; i++) {
                createAnnotationProperty(annotation_uris[i]);
            }
        } else {
            throw new NullPointerException("The set of annotation property URIs you "
                    + "provided is null.");
        }
    }
    
    

    public void includeOntClass(OntEntity ontEntity) {
        ontEntity.createOntClass(this);
    }

    public void includeOntClasses(OntEntity... ontEntities) {
        if (ontEntities != null) {
            for (int i = 0; i < ontEntities.length; i++) {
                this.includeOntClass(ontEntities[i]);
            }
        } else {
            throw new NullPointerException("The set of entities you provided is null.");
        }
    }
}
