# jenkins-build-tool

Configuration Guid
Jenkins:
1. Install Windows Exe Runner Plugin https://wiki.jenkins-ci.org/display/JENKINS/Windows+Exe+Runner+Plugin

Environment:
1. MsBuild 
    - Install from url https://www.microsoft.com/en-us/download/details.aspx?id=48159
    - Update path to MsBuild runner in Jenkins using folowing path:
        Manage Jenkins > Global tool configuration > Windows Exe Instalations
        Fill settings in folowing way
         Name: msbuild
         Path to Exe: C:\Program Files (x86)\MSBuild\14.0\Bin\MSBuild.exe
2. FxCopCmd Runner
   - Install from url https://fxcopinstaller.codeplex.com/
   - Update path to FxCopCmd runner in Jenkins using folowing path:
     Manage Jenkins > Global tool configuration > Windows Exe Instalations
         Name: fxcop
         Path to Exe: C:\Program Files (x86)\Microsoft Fxcop 10.0\FxCopCmd.exe
Jenkins Plugins 
  Pipeline Utility Steps
  NUnit plugin
  

