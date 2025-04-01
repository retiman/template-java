# ADR 001: No Dev Containers

## Status

Accepted

## Context

Should we use dev containers in this repo?

## Decision

No.

Reasons:

= Dev containers are great for setting up a shared dev environment, but not so great for bringing in your personal dev settings.  There's not an easy way define a shared build toolkit but allow for someone to bring in their own bash settings, aliases, or other tooling.
- Dev containers are slow on Windows or OSX.
