# Plagiarism Detector #

This is a plagiarism detector that calculates the percentage that N-tuples of words in file 1 appear in that of file 2, synonyms words provided in the synonyms list file will be treated as the same.  
The program takes 2 input files as well as a synonyms file. Default tuple size if not provided: 3 words.

For example, if _jog_ is inputed as a synonyms of _run_, then inputs "Go for a run." and "go for a JOG?" will result in 100% similarity.

"Go for a run." = (go for a, for a run) = (go for a, for a jog) = "go for a JOG?"

However if _went_ is not provided as a synonyms of _go_, inputs "Go for a run." and "Went for a run." will result in only 50% similarity.

"Go for a run." = (go for a, for a run)  
"Went for a run." = (went for a, for a run)  
Only 1 tuple of input1 appears in input2, so similarity = 50%;

## Usage: ##
Java -jar PlagiarismDetector.jar \<synonyms file path> <file 1 path> <file 2 path> \<tuple size>(optional)
