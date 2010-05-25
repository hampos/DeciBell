/**
 *  Class : OntEntity
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

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * <p  align="justify" style="width:60%">
 * The DeciBell ontology and its ontological entities establish a formal
 * representation of the knowledge by a set of concepts manipulated by DeciBell
 * and the relationships between those concepts. The DeciBell ontology provides
 * all necessary specifications for the conceptualization of abstract notions
 * such as a database table or a table column paving the way towards a consistent
 * vocabulary able to model a set of (logical) propositions.
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OntEntity {

    protected static final String _DECIBELL_BASE = "http://www.kinkydesign.org/decibell/ver.0.1/#%s";
    protected static final String _DECIBELL_DATATYPES = "http://www.kinkydesign.org/decibell/datatypes/#%s";
    public static final String DECIBELL_BASE = String.format(_DECIBELL_BASE, "");
    public static final String DECIBELL_DATATYPES = String.format(_DECIBELL_DATATYPES, "");
    
    protected Resource resource;

        public OntEntity() {
    }

    public OntEntity(Resource resource) {
        this.resource = resource;
    }

    public OntClass createOntClass(OntObject model) {
        OntClass clazz = model.createClass(getURI());
        clazz.setVersionInfo("0.1");
        return clazz;
    }


    public String getURI() {
        return resource.getURI();
    }        


    public Resource getResource() {
        return resource;
    }

    
    
}
