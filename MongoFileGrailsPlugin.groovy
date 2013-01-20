class MongoFileGrailsPlugin {
    def version = "1.1.1.LNDS"
    def grailsVersion = "2.2 > *"
    def dependsOn = [mongodb:"1.1.0.GA"]
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "MongoFile Plugin 2.2"
    def author = "Eduardo Díaz"
    def authorEmail = "ediaz@lnds.net"
    def description = '''\
This is a fix version of the MongoFile plugin created by craigraw.

The MongoFile plugin provides a MongoFileService that saves, retrieves and deletes files from a MongoDB file store. Furthermore, the domain classes have methods injected to handle these files.

Each file is stored in a MongoDB collection (bucket), named after the domain class name. This plugin depends on the MongoDB plugin.
'''
    def documentation = "https://github.com/quirklabs/grails-mongofile/blob/master/README.md"
    def license = "APACHE"
    def organization = [ name: "LNDS", url: "http://www.lnds.net" ]
    def developers = [[ name: "Eduardo Díaz" ]]
    def issueManagement = [ system: "github", url: "https://github.com/lnds/grails-mongofile/issues" ]
    def scm = [ url: "https://github.com/lnds/grails-mongofile" ]

    def doWithDynamicMethods = { ctx ->
        for(domainClass in application.domainClasses) {
            domainClass.metaClass.mongoFileExists = { String fieldName = '' -> 
                getMongoFile(fieldName) != null
            }
            
            domainClass.metaClass.getMongoFile = { String fieldName = '' -> 
                def mongoFileService = ctx.getBean("mongoFileService")
                if(mongoFileService) {
                    return mongoFileService.getFile(delegate.getClass(), id, fieldName)
                }
                
                null
            }
            
            domainClass.metaClass.saveMongoFile = { org.springframework.web.multipart.MultipartFile file, String fieldName = '' -> 
                def mongoFileService = ctx.getBean("mongoFileService")
                if(mongoFileService) {
                    mongoFileService.saveFile(file, delegate.getClass(), id, fieldName)
                }
            }
            
            domainClass.metaClass.saveMongoFile = { byte[] fileContents, String fileName, String fieldName = '' -> 
                def mongoFileService = ctx.getBean("mongoFileService")
                if(mongoFileService) {
                    mongoFileService.saveFile(fileContents, fileName, delegate.getClass(), id, fieldName)
                }
            }       
            
            domainClass.metaClass.saveMongoFile = { InputStream inputStream, String fileName, String fieldName = '' -> 
                def mongoFileService = ctx.getBean("mongoFileService")
                if(mongoFileService) {
                    mongoFileService.saveFile(inputStream, fileName, delegate.getClass(), id, fieldName)
                }
            }                 
            
            domainClass.metaClass.deleteMongoFile = { String fieldName = '' -> 
                def mongoFileService = ctx.getBean("mongoFileService")
                if(mongoFileService) {
                    mongoFileService.deleteFile(delegate.getClass(), id, fieldName)
                }
            }
        }
    }
}
