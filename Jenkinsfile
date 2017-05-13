#!groovy
def repos = ['https://github.com/khdevnet/jenkins-build-tool.git',
             'https://github.com/khdevnet/REST.git'];
parallel repos {repo -> [/* thread label */repo, {
    node {
      git "${repo}"
    }
}]}
