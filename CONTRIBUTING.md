# How to contribute

Thank you for taking time to contribute to this repository! This guide should help you effectively do so by following the systems already in place for maintaining the repository.

## Table of contents

* [Code of conduct](#code-of-conduct)
* [Raising an issue](#raising-an-issue)
* [Making a pull request](#making-a-pull-request)
* [Coding standards](#coding-standards)
  * [Code style](#code-style)
  * [Testing](#testing)
* [External resources](#external-resources)

## Code of conduct

This repository and everyone contributing to is subject to follow the adopted [code of conduct](CODE_OF_CONDUCT.md). Should you choose to contribute, you will be expected to uphold this code. Please report any violations to [@gkapfham](https://github.com/gkapfham) or [@philmcminn](https://github.com/philmcminn).

## Raising an issue

First, check the [Issue Tracker](https://github.com/AVMf/avmf/issues) to make sure it has not already been made there. If it would be a new issue, go ahead and [raise it](https://github.com/AVMf/avmf/issues/new)! We just ask that you:
* give it a descriptive title
* provide any relevant information
* add any labels that apply to it
* reference any other related issues

## Making a pull request

When making a [pull request](https://github.com/AVMf/avmf/pulls), we ask that you do the following:
* give it a descriptive title
* include language that will close all issues it is proposing fixes for
* describes any issues that it is proposing to fix that are not in the issue tracker to give a greater idea as to why the pull request should be approved
* fully describes all of the changes it is proposing and why succinctly states why they should be approved
* add any labels that apply to it
* includes all relevant updates to documentation if warranted by the proposed changes

## Coding standards

All of the following subsections must be adhered to:

### Code style

All of the code in the repository is written in Java and must conform to the [Google check style for Java](https://google.github.io/styleguide/javaguide.html) so that all code is in a single form. Currently, the requirement for having Javadoc comments is disabled, but they should be included with new code.

### Testing

All pull requests should either maintain the current level of code coverage or increase it. Any amount of coverage lost will not be accepted and the pull request will not be merged. Test cases should be made to cover any new code added. This should be kept in mind while writing code so that it can tested easily and as much of it can be covered as possible. The goal will always be to have as high of a code coverage as possible and ultimately everything, or as much as possible if not everything can be refactored to be covered.

## External resources

If you would like more information on the repository and project as whole, feel free to read the [paper](https://mcminn.io/publications/c43.html) associated with it or visit its [website](http://avmframework.org/).

Thanks for taking the time to read this!