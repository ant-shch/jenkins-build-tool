#!groovy
def repos = ['buildtools':'https://github.com/khdevnet/jenkins-build-tool.git',
             'main':'https://github.com/khdevnet/jenkins-build-tool.git'];
parallel repos {repo -> [/* thread label */repo, {
    node {
      git "${repo.value}"
    }
}]}
