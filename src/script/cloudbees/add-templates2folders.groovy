﻿/* groovylint-disable CompileStatic, CouldBeElvis, DuplicateNumberLiteral, ExplicitCallToEqualsMethod, ExplicitTreeSetInstantiation, LineLength, SpaceAroundClosureArrow, UnnecessaryGetter, UnnecessarySetter */
package script.cloudbees

import jenkins.model.*
import hudson.model.*
import com.cloudbees.hudson.plugins.modeling.Model
import com.cloudbees.hudson.plugins.folder.Folder
import com.cloudbees.hudson.plugins.folder.properties.SubItemFilterProperty

// PARAMETER
// Note the structure of the Folder item in terms of file system
pFolders = 'job/TemplateFolder/job/templateFolder/job/OutputFolder3/'

Set<String> allowedTypes = new TreeSet <String>()
processingFlag = false

// A - Getting items from the jenkins instance

jenkins = Jenkins.instanceOrNull
jenkinsTemplates = jenkins.getAllItems(Model)
jenkinsFolders = jenkins.getAllItems(Folder)

// B - Preparing templates element for assignating them to pFolder

if (jenkinsTemplates.size() > 0) {
    jenkinsTemplates.each{ template ->
        // println "[DEBUG]: "+template.id
        // Each job Templetes is consider by jenkins as 1 type of jobs
        allowedTypes.add(template.id)
    }
} else {
    println '[ERROR]: There are not job templates available in this instance'
}

//c - Assigning templates  to pFolder

if (jenkinsFolders.size() > 0) {
    //if the instance contains template
    if (allowedTypes.size() > 0) {
        jenkinsFolders.each { folder->
            //filter example
            if ((folder.url).equals(pFolders)) {
                // println "[DEBUG]: "+folder.url
                filterProp = new SubItemFilterProperty(allowedTypes)
                folder.getProperties().add(filterProp)
                jenkins.save()
                if (!processingFlag) {
                    processingFlag = true
                }
                println "[INFO]: This instance's Templates has been added as Item Filter property for pFolders: '$pFolders'"
                println 'Note: Just has been added those templates at the same node level of pFolder or highest level in terms of tree depth'
            }
        }
    }
} else {
    println '[ERROR]: There are not folders available in this instance'
}

if (!processingFlag) {
    println '[ERROR]: The folder url [$pFolders] is not available in this instance'
}
