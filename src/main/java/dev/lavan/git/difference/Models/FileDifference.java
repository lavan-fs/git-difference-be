package dev.lavan.git.difference.Models;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileDifference {
     public String getChangeKind() {
          return changeKind;
     }

     public void setChangeKind(String changeKind) {
          this.changeKind = changeKind;
     }

     public List<Hunk> getHunks() {
          return hunks;
     }

     public void setHunks(List<Hunk> hunks) {
          this.hunks = hunks;
     }

     public FileDetail getBaseFile() {
          return baseFile;
     }

     public void setBaseFile(FileDetail baseFile) {
          this.baseFile = baseFile;
     }

     public FileDetail getHeadFile() {
          return headFile;
     }

     public void setHeadFile(FileDetail headFile) {
          this.headFile = headFile;
     }

     String changeKind;
     FileDetail headFile;
     FileDetail baseFile;
     List<Hunk> hunks;
}




