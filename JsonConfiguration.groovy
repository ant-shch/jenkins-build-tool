import groovy.json.JsonSlurper

class Repository {
      Repository(name,url)
      {
         this.Name = name
         this.Url = url
      }
      def Name
      def Url
}

class Configuration {
      Configuration(repositories) {          
        this.Repositories = repositories
      }
   
      List<Repository> Repositories   
}

class JsonConfigurationRepository {
   Configuration Get(def path) { 
         def configurationFile = new File(path)
         def object = new JsonSlurper().parseText(configurationFile.text);
         List<Repository> repositories = new ArrayList<Repository>();
         for(def rep : object.repositories)  {
             repositories.add(new Repository(rep.name, rep.url))
         }
         return new Configuration(repositories) 
       }
}

return new JsonConfigurationRepository();
