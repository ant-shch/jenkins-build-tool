# jenkins-build-tool

Configuration guide
Jenkins:

1. Install plugins
   - Windows Exe Runner Plugin https://wiki.jenkins-ci.org/display/JENKINS/Windows+Exe+Runner+Plugin
   - Email-ext Plugin https://wiki.jenkins-ci.org/display/JENKINS/Email-ext+plugin
   - EnvInject Plugin https://wiki.jenkins-ci.org/display/JENKINS/EnvInject+Plugin
   - NUnit plugin https://wiki.jenkins-ci.org/display/JENKINS/NUnit+Plugin
   - Pipeline https://wiki.jenkins-ci.org/display/JENKINS/Pipeline+Plugin
   - Pipeline Utility Steps https://wiki.jenkins-ci.org/display/JENKINS/Pipeline+Utility+Steps+Plugin

2. Environment:
    MsBuild 
      - Install from url https://www.microsoft.com/en-us/download/details.aspx?id=48159
      - Update path to MsBuild runner in Jenkins using folowing path:
          Manage Jenkins > Global tool configuration > Windows Exe Instalations
          Fill settings in folowing way
           Name: msbuild
           Path to Exe: C:\Program Files (x86)\MSBuild\14.0\Bin\MSBuild.exe

    FxCopCmd Runner
     - Install from url https://fxcopinstaller.codeplex.com/
     - Update path to FxCopCmd runner in Jenkins using folowing path:
       Manage Jenkins > Global tool configuration > Windows Exe Instalations
           Name: fxcop
           Path to Exe: C:\Program Files (x86)\Microsoft Fxcop 10.0\FxCopCmd.exe

    NUnit Test Runner
     - Update path to NUnit runner in Jenkins using folowing path:
       Manage Jenkins > Global tool configuration > Windows Exe Instalations
           Name: nunit
           Path to Exe: ${WORKSPACE}\jenkins-build-tool\buildtools\nunit\nunit3-console.exe

    NuGet Packages Installer
     - Update path to NuGet runner in Jenkins using folowing path:
       Manage Jenkins > Global tool configuration > Windows Exe Instalations
           Name: nuget
           Path to Exe: ${WORKSPACE}\jenkins-build-tool\buildtools\.nuget\NuGet.exe  
 3. Configure Job:
    - Job > Configure > Prepare an environment for the run
    - Add path to components COMPONENTS = ["https://github.com/yourcomponentpath.git", "https\://github.com/khdevnet/jenkins-build-tool.git" ]
    
 4. BuildConfiguration.json 
    - Add BuildConfiguration.json  to the root folder of your component
    - Example you can find in "https\://github.com/khdevnet/jenkins-build-tool.git"
