#Simple utility for filling in missing data from Shuffleboard recording files.
#Shuffleboard recordings contain missing values when values are not changing, this function fills them in.
#Shuffleboard recordings should probably be converted to CSV before being loaded into R.

fillShuffleboardRecording <- function(dat) {
  for(col in colnames(dat)[colnames(dat) != "Event" || 
                           colnames(dat) != "Event.Description" || 
                           colnames(dat) != "Event.Severity"]) {
    for(i in 2:nrow(dat)) {
      if(is.na(dat[i, col])) {
        dat[i, col] <- dat[i-1, col]
      }
    }
  }
  return(dat)
}
