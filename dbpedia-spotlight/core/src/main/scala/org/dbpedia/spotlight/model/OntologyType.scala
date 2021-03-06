/*
 * Copyright 2011 Pablo Mendes, Max Jakob
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Check our project website for information on how to acknowledge the authors and how to contribute to the project: http://spotlight.dbpedia.org
 */

package org.dbpedia.spotlight.model

import java.lang.Short

/**
 * Representation of types (DBpedia, Freebase, Schema.org, etc.)
 *
 * @author maxjakob
 * @author Joachim Daiber
 * @author pablomendes (introduced and fixed bug for OntologyType.equals :)
 * @author dirk.weissenborn (introduced opencyc)
 */

trait OntologyType extends Serializable {
      def getFullUri: String
      def typeID: String = "OntologyTypeUnknown"

      var id: Short = 0.toShort

      override def hashCode() : Int = {
        typeID.hashCode()
      }

      override def equals(other : Any) : Boolean = {
          if (other==null)
               false
          else
            other match {
                case o: OntologyType => o.typeID != null && o.typeID.equals(typeID)
                case _ => false;
            }
      }

      override def toString = typeID
}

/**
 * Types from the DBpedia ontology (hierarchical)
 */


@SerialVersionUID(8037662401509425326L)
class DBpediaType(var name : String) extends OntologyType {

    name = name.replace(DBpediaType.DBPEDIA_ONTOLOGY_PREFIX, "")

    name = name.replace("DBpedia:","")

    name = name.capitalize

    name = name.replaceAll(" ([a-zA-Z])", "$1".toUpperCase).trim

    def equals(that : DBpediaType) : Boolean = {
        name.equalsIgnoreCase(that.name)
    }

    override def getFullUri =  if (name.length >= 4 && name.substring(0,4).equalsIgnoreCase("http")) name else DBpediaType.DBPEDIA_ONTOLOGY_PREFIX + name
    override def typeID = if (name.length >= 4 && name.substring(0,4).equalsIgnoreCase("http")) name else "DBpedia:".concat(name)

}

object DBpediaType {
    val DBPEDIA_ONTOLOGY_PREFIX = SpotlightConfiguration.getDbpediaOntology()
    val UNKNOWN = new DBpediaType("unknown")
}


/**
 * Types from Freebase: non-hierarchical, grouped into domains.
 */

@SerialVersionUID(8037662401509425325L)
class FreebaseType(val domain: String, val typeName: String) extends OntologyType {

  override def getFullUri = FreebaseType.FREEBASE_RDF_PREFIX + domain + "." + typeName
  override def typeID = {
    var typeID = "Freebase:/" + domain

    if(typeName != null) {
      typeID += "/" + typeName
    }

    typeID
  }
}

object FreebaseType {

  def fromTypeString(typeString: String) : FreebaseType = {
    val typeParts: Array[String] = typeString.replace(FREEBASE_RDF_PREFIX, "").split("/")

    var domain: String = null
    var theType: String = null
    typeParts.length match {
      case 0 =>
      case 1 => domain = typeParts(0)
      case 2 => domain = typeParts(1)
      case _ => {domain = typeParts(1); theType = typeParts(2)}
    }

    new FreebaseType(domain, theType)
  }

  val FREEBASE_RDF_PREFIX = "http://rdf.freebase.com/ns"
}

@SerialVersionUID(8037662401509425324l)
class SchemaOrgType(var name : String) extends OntologyType {

    name = name.replace(SchemaOrgType.SCHEMAORG_PREFIX, "")

    def equals(that : SchemaOrgType) : Boolean = {
        name.equalsIgnoreCase(that.name)
    }

    override def getFullUri = SchemaOrgType.SCHEMAORG_PREFIX + name
    override def typeID = "Schema:" + name

   // override def toString = "%s/%s".format(SchemaOrgType.SCHEMAORG_PREFIX,name)

}

object SchemaOrgType {
    val SCHEMAORG_PREFIX = "http://schema.org/"
}


@SerialVersionUID(8037662401509425323l)
class OpenCycConcept(var name : String) extends OntologyType {

    name = name.replace(OpenCycConcept.OPENCYCCONCEPT_PREFIX, "")

    def equals(that : OpenCycConcept) : Boolean = {
        name.equalsIgnoreCase(that.name)
    }

    override def getFullUri = OpenCycConcept.OPENCYCCONCEPT_PREFIX + name
    override def typeID = "OpenCyc:" + name

}

object OpenCycConcept {
    val OPENCYCCONCEPT_PREFIX = "http://sw.opencyc.org/concept/"
}

@SerialVersionUID(80376624015095324l)
class WikidataType(var name : String) extends OntologyType {

  name = name.replace(WikidataType.WIKIDATATYPE_PREFIX, "")

  def equals(that : WikidataType) : Boolean = {
    name.equalsIgnoreCase(that.name)
  }

  override def getFullUri = WikidataType.WIKIDATATYPE_PREFIX + name
  override def typeID = "Wikidata:" + name


}

object WikidataType {
  val WIKIDATATYPE_PREFIX = "http://www.wikidata.org/entity/"
}



@SerialVersionUID(8037662409415095324l)
class DULType(var name : String) extends OntologyType {

  name = name.replace(DULType.DULTYPE_PREFIX, "")

  def equals(that : DULType) : Boolean = {
    name.equalsIgnoreCase(that.name)
  }

  override def getFullUri = DULType.DULTYPE_PREFIX + name
  override def typeID = "DUL:" + name


}

object DULType {
  val DULTYPE_PREFIX = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#"
}


