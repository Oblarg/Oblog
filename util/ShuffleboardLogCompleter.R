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
