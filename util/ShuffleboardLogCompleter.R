#Simple utility for filling in missing data from Shuffleboard recording files.
#Shuffleboard recordings contain missing values when values are not changing, this function fills them in.
#Shuffleboard recordings must be converted to .CSV for this tool to work on them.

#To use this script, ensure that R is installed on your machine and has been added to the PATH.
#Then, move this script and the associated .bat file into the directory of the shuffleboard recordings.
#Finally, run the .bat (or .sh) file.  If nothing happens, run it as an administrator (Windows sucks).

#Warning: This script will fill *all* .csv files in the directory.  Be sure there are no other
#.csv files located in the directory that you do not want modified.

args<-commandArgs(trailingOnly = TRUE)

fillShuffleboardRecording <- function(dat) {
  for(col in colnames(dat)[colnames(dat) != "Event" || 
                           colnames(dat) != "Event.Description" || 
                           colnames(dat) != "Event.Severity"]) {
    for(i in 2:nrow(dat)) {
      if(is.na(dat[i, col]) || dat[i, col] == "") {
        dat[i, col] <- dat[i-1, col]
      }
    }
  }
  return(dat)
}


fillFile <- function(file) {
  dat <- read.csv(file)
  dat <- fillShuffleboardRecording(dat)
  write.csv(dat, file = file)
}


files <- list.files(path = as.character(args[1]),pattern = "*.csv")
lapply(files, fillFile)