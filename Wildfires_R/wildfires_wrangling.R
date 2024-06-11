# Frankie Wilson - CA Wilfdires Project 
# wildfires_wrangling.R 

# Setting up the data for the main project + attaching any needed libraries. 
# Doing so here to keep the qmd/html more presentable. 

# For Loops and functions are in play: the functions are lines 77-90.
# There's really only 3 for loops, but they're repeated throughout the script. 
# I tried condensing all of this down to a single function, but was running into too
# many errors. 

# suppressing warnings when loading libraries 
defaultW <- getOption("warn") 
options(warn = -1) 

suppressPackageStartupMessages(library(tidyverse))
suppressPackageStartupMessages(library(janitor))
suppressPackageStartupMessages(library(ggplot2))
suppressPackageStartupMessages(library(dplyr))
suppressPackageStartupMessages(library(lubridate))
suppressPackageStartupMessages(library(sf))
suppressPackageStartupMessages(library(mapview))
suppressPackageStartupMessages(remotes::install_github("r-spatial/mapview"))
suppressPackageStartupMessages(library(leafsync))
suppressPackageStartupMessages(library(leaflet.extras2))
suppressPackageStartupMessages(library(leafpop))
suppressPackageStartupMessages(library(leafem))
suppressPackageStartupMessages(library(RColorBrewer))
options(warn = defaultW) # turning warnings back on 

# to avoid scientific notation in plots 
options(scipen = 999)

#load and clean data
fod <- read.csv("~/Winter 2023/309/fod/fod.csv")
fod <- janitor::clean_names(fod)

# Convert dates to date types 
fod <- fod |>
  mutate(discovery_date = as.Date(discovery_date, format = "%m/%d/%Y"))
fod <- fod |>
  mutate(cont_date = as.Date(cont_date, format = "%m/%d/%Y"))

# split into smaller, more workable datasets 
fod_CA <- fod |> filter(state == "CA")
#second verse, same as the first 
CA_2020 <- fod_CA |> filter(fire_year == 2020)
CA_2019 <- fod_CA |> filter(fire_year == 2019)
CA_2018 <- fod_CA |> filter(fire_year == 2018)
CA_2017 <- fod_CA |> filter(fire_year == 2017)
CA_2016 <- fod_CA |> filter(fire_year == 2016)
CA_2015 <- fod_CA |> filter(fire_year == 2015)
CA_2014 <- fod_CA |> filter(fire_year == 2014)
CA_2013 <- fod_CA |> filter(fire_year == 2013)
CA_2012 <- fod_CA |> filter(fire_year == 2012)
CA_2011 <- fod_CA |> filter(fire_year == 2011)
CA_2010 <- fod_CA |> filter(fire_year == 2010)
CA_2009 <- fod_CA |> filter(fire_year == 2009)
CA_2008 <- fod_CA |> filter(fire_year == 2008)
CA_2007 <- fod_CA |> filter(fire_year == 2007)
CA_2006 <- fod_CA |> filter(fire_year == 2006)
CA_2005 <- fod_CA |> filter(fire_year == 2005)
CA_2004 <- fod_CA |> filter(fire_year == 2004)
CA_2003 <- fod_CA |> filter(fire_year == 2003)
CA_2002 <- fod_CA |> filter(fire_year == 2002)
CA_2001 <- fod_CA |> filter(fire_year == 2001)
CA_2000 <- fod_CA |> filter(fire_year == 2000)
CA_1999 <- fod_CA |> filter(fire_year == 1999)
CA_1998 <- fod_CA |> filter(fire_year == 1998)
CA_1997 <- fod_CA |> filter(fire_year == 1997)
CA_1996 <- fod_CA |> filter(fire_year == 1996)
CA_1995 <- fod_CA |> filter(fire_year == 1995)
CA_1994 <- fod_CA |> filter(fire_year == 1994)
CA_1993 <- fod_CA |> filter(fire_year == 1993)
CA_1992 <- fod_CA |> filter(fire_year == 1992)


# function to add hours and minutes to a discovery date 
# using as.POSIXct() for cont because otherwise it doesn't work past 2018
discoveryTime <- function(df, x){
  {df}$discovery_date[x] +
    hours(({df}$discovery_time[x])%/%100) + 
    minutes(({df}$discovery_time[x]%%100))
}

# function to add hours and minutes to a cont date 
contTime <- function(df, x){
  as.POSIXct({df}$cont_date[x]) +
    hours(({df}$cont_time[x])%/%100) + 
    minutes(({df}$cont_time[x]%%100))
}


# CA_2020
# utilizing the discoveryTime and contTime functions and binding the results as new cols 
discovery <- as.Date(character(0))
for(i in 1:10198){
  discovery<- append(discovery, discoveryTime(CA_2020, i))
}
# converting to UTC instead of EST 
attr(discovery, "tzone") <- "UTC"
CA_2020 <- CA_2020 |> cbind(discovery)

# discovery done, now for containment 
containment <- as.Date(character(0))
for(i in 1:10198){
  containment <- append( containment, contTime(CA_2020, i))
}
# convert to UTC as well
attr(containment, "tzone") <- "UTC"
CA_2020 <- CA_2020 |> cbind(containment)

# getting the length of a fire in terms of minutes
fire_duration <- vector(length = 10198)
for(i in 1:10198) {
  fire_duration =
    as.numeric(CA_2020$containment - CA_2020$discovery)
}
CA_2020 <- CA_2020 |> cbind(fire_duration)

# and finally calculating the severity 
fire_severity <- vector(length=10198)
fire_severity = (CA_2020$fire_duration)*(CA_2020$fire_size)
CA_2020 <- CA_2020 |> cbind(fire_severity)




# CA_2019 (exact same as above but updated for the new dataset)
# vectors need to be reset each time so data isn't being continously added at the end of any prev data
# discovery
discovery <- as.Date(character(0))
for(i in 1:6454){
  discovery<- append(discovery, discoveryTime(CA_2019, i))
}
attr(discovery, "tzone") <- "UTC"
CA_2019 <- CA_2019 |> cbind(discovery)

#containment
containment <- as.Date(character(0))
for(i in 1:6454){
  containment <- append( containment, contTime(CA_2019, i))
}
attr(containment, "tzone") <- "UTC"
CA_2019 <- CA_2019 |> cbind(containment)

# duration
fire_duration <- vector(length = 6454)
for(i in 1:6454) {
  fire_duration =
    as.numeric(CA_2019$containment - CA_2019$discovery)/60
}
CA_2019 <- CA_2019 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=6454)
fire_severity = (CA_2019$fire_duration)*(CA_2019$fire_size)
CA_2019 <- CA_2019 |> cbind(fire_severity)





# CA_2018
# discovery
discovery <- as.Date(character(0))
for(i in 1:9488){
  discovery<- append(discovery, discoveryTime(CA_2018, i))
}
attr(discovery, "tzone") <- "UTC"
CA_2018 <- CA_2018 |> cbind(discovery)

#containment
containment <- as.POSIXct(character(0), format = "%Y-%m-%d %H:%M:%S")
for(i in 1:9488){
  containment <- append(containment, contTime(CA_2018, i))
}
attr(containment, "tzone") <- "UTC"
CA_2018 <- CA_2018 |> cbind(containment)

# duration
fire_duration <- vector(length = 9488)
for(i in 1:9488) {
  fire_duration =
    as.numeric(CA_2018$containment - CA_2018$discovery)/60
}
CA_2018 <- CA_2018 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=9488)
fire_severity = (CA_2018$fire_duration)*(CA_2018$fire_size)
CA_2018 <- CA_2018 |> cbind(fire_severity)





# CA_2017
# discovery
discovery <- as.Date(character(0))
for(i in 1:9537){
  discovery<- append(discovery, discoveryTime(CA_2017, i))
}
attr(discovery, "tzone") <- "UTC"
CA_2017 <- CA_2017 |> cbind(discovery)

#containment
containment <- as.Date(character(0))
for(i in 1:9537){
  containment <- append( containment, contTime(CA_2017, i))
}
attr(containment, "tzone") <- "UTC"
CA_2017 <- CA_2017 |> cbind(containment)

# duration
fire_duration <- vector(length = 9537)
for(i in 1:9537) {
  fire_duration =
    as.numeric(CA_2017$containment - CA_2017$discovery)/60
}
CA_2017 <- CA_2017 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=9537)
fire_severity = (CA_2017$fire_duration)*(CA_2017$fire_size)
CA_2017 <- CA_2017 |> cbind(fire_severity)





# CA_2016
# discovery
discovery <- as.Date(character(0))
for(i in 1:7882){
  discovery<- append(discovery, discoveryTime(CA_2016, i))
}
attr(discovery, "tzone") <- "UTC"
CA_2016 <- CA_2016 |> cbind(discovery)

#containment
containment <- as.Date(character(0))
for(i in 1:7882){
  containment <- append( containment, contTime(CA_2016, i))
}
attr(containment, "tzone") <- "UTC"
CA_2016 <- CA_2016 |> cbind(containment)

# duration
fire_duration <- vector(length = 7882)
for(i in 1:7882) {
  fire_duration =
    as.numeric(CA_2016$containment - CA_2016$discovery)/60
}
CA_2016 <- CA_2016 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=7882)
fire_severity = (CA_2016$fire_duration)*(CA_2016$fire_size)
CA_2016 <- CA_2016 |> cbind(fire_severity)





# CA_2015
# discovery
discovery <- as.Date(character(0))
for(i in 1:7355){
  discovery<- append(discovery, discoveryTime(CA_2015, i))
}
attr(discovery, "tzone") <- "UTC"
CA_2015 <- CA_2015 |> cbind(discovery)

#containment
containment <- as.Date(character(0))
for(i in 1:7355){
  containment <- append( containment, contTime(CA_2015, i))
}
attr(containment, "tzone") <- "UTC"
CA_2015 <- CA_2015 |> cbind(containment)

# duration
fire_duration <- vector(length = 7355)
for(i in 1:7355) {
  fire_duration =
    as.numeric(CA_2015$containment - CA_2015$discovery)/60
}
CA_2015 <- CA_2015 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=7355)
fire_severity = (CA_2015$fire_duration)*(CA_2015$fire_size)
CA_2015 <- CA_2015 |> cbind(fire_severity)





# CA_2010
# discovery
discovery <- as.Date(character(0))
for(i in 1:8304){
  discovery<- append(discovery, discoveryTime(CA_2010, i))
}
attr(discovery, "tzone") <- "UTC"
CA_2010 <- CA_2010 |> cbind(discovery)

#containment
containment <- as.Date(character(0))
for(i in 1:8304){
  containment <- append( containment, contTime(CA_2010, i))
}
attr(containment, "tzone") <- "UTC"
CA_2010 <- CA_2010 |> cbind(containment)

# duration
fire_duration <- vector(length = 8304)
for(i in 1:8304) {
  fire_duration =
    as.numeric(CA_2010$containment - CA_2010$discovery)/60
}
CA_2010 <- CA_2010 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=8304)
fire_severity = (CA_2010$fire_duration)*(CA_2010$fire_size)
CA_2010 <- CA_2010 |> cbind(fire_severity)





# CA_2005
# discovery
discovery <- as.Date(character(0))
for(i in 1:10219){
  discovery<- append(discovery, discoveryTime(CA_2005, i))
}
attr(discovery, "tzone") <- "UTC"
CA_2005 <- CA_2005 |> cbind(discovery)

#containment
containment <- as.Date(character(0))
for(i in 1:10219){
  containment <- append( containment, contTime(CA_2005, i))
}
attr(containment, "tzone") <- "UTC"
CA_2005 <- CA_2005 |> cbind(containment)

# duration
fire_duration <- vector(length = 10219)
for(i in 1:10219) {
  fire_duration =
    as.numeric(CA_2005$containment - CA_2005$discovery)/60
}
CA_2005 <- CA_2005 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=10219)
fire_severity = (CA_2005$fire_duration)*(CA_2005$fire_size)
CA_2005 <- CA_2005 |> cbind(fire_severity)





# CA_2000
# discovery
discovery <- as.Date(character(0))
for(i in 1:6970){
  discovery<- append(discovery, discoveryTime(CA_2000, i))
}
attr(discovery, "tzone") <- "UTC"
CA_2000 <- CA_2000 |> cbind(discovery)

#containment
containment <- as.Date(character(0))
for(i in 1:6970){
  containment <- append( containment, contTime(CA_2000, i))
}
attr(containment, "tzone") <- "UTC"
CA_2000 <- CA_2000 |> cbind(containment)

# duration
fire_duration <- vector(length = 6970)
for(i in 1:6970) {
  fire_duration =
    as.numeric(CA_2000$containment - CA_2000$discovery)/60
}
CA_2000 <- CA_2000 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=6970)
fire_severity = (CA_2000$fire_duration)*(CA_2000$fire_size)
CA_2000 <- CA_2000 |> cbind(fire_severity)





# CA_1995
# discovery
discovery <- as.Date(character(0))
for(i in 1:7381){
  discovery<- append(discovery, discoveryTime(CA_1995, i))
}
attr(discovery, "tzone") <- "UTC"
CA_1995 <- CA_1995 |> cbind(discovery)

#containment
containment <- as.Date(character(0))
for(i in 1:7381){
  containment <- append( containment, contTime(CA_1995, i))
}
attr(containment, "tzone") <- "UTC"
CA_1995 <- CA_1995 |> cbind(containment)

# duration
fire_duration <- vector(length = 7381)
for(i in 1:7381) {
  fire_duration =
    as.numeric(CA_1995$containment - CA_1995$discovery)/60
}
CA_1995 <- CA_1995 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=7381)
fire_severity = (CA_1995$fire_duration)*(CA_1995$fire_size)
CA_1995 <- CA_1995 |> cbind(fire_severity)





# CA_1992
# discovery
discovery <- as.Date(character(0))
for(i in 1:10831){
  discovery<- append(discovery, discoveryTime(CA_1992, i))
}
attr(discovery, "tzone") <- "UTC"
CA_1992 <- CA_1992 |> cbind(discovery)

#containment
containment <- as.Date(character(0))
for(i in 1:10831){
  containment <- append( containment, contTime(CA_1992, i))
}
attr(containment, "tzone") <- "UTC"
CA_1992 <- CA_1992 |> cbind(containment)

# duration
fire_duration <- vector(length = 10831)
for(i in 1:10831) {
  fire_duration =
    as.numeric(CA_1992$containment - CA_1992$discovery)/60
}
CA_1992 <- CA_1992 |> cbind(fire_duration)

# severity
fire_severity <- vector(length=10831)
fire_severity = (CA_1992$fire_duration)*(CA_1992$fire_size)
CA_1992 <- CA_1992 |> cbind(fire_severity)