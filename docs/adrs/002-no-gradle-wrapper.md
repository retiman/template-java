# ADR 002: No Gradle Wrapper

## Status

Accepted

## Context

Should we use Gradle wrapper?

## Decision

No.

Reasons:

- It's annoying to type `./` in front of every Gradle command.
- It's unnecessary to Make the Gradle version consistent in this way.  The CI system will already catch "but it works on my machine" issues.
- It's not appropriate to store jar files in Git.
- Developers are already going to have their own version of Java and Gradle installed via `sdkman`, and they can refer to the CI workflows to see which versions to get.
