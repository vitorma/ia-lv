#!/bin/bash
grep fitness $1 | awk -F "</?fitness>" '{print $2}'

