package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.ExceptionTypeEnum;

@Entity
public class ExceptionReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exceptionReportId;
    @Enumerated(EnumType.STRING)
    private ExceptionTypeEnum exceptionType;
    @Column(length = 255)
    private String message;
    @Column(nullable = false)
    private boolean resolved;

    public ExceptionReport() {
    }
    
    public ExceptionTypeEnum getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(ExceptionTypeEnum exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public Long getExceptionReportId() {
        return exceptionReportId;
    }

    public void setExceptionReportId(Long exceptionReportId) {
        this.exceptionReportId = exceptionReportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exceptionReportId != null ? exceptionReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the exceptionReportId fields are not set
        if (!(object instanceof ExceptionReport)) {
            return false;
        }
        ExceptionReport other = (ExceptionReport) object;
        if ((this.exceptionReportId == null && other.exceptionReportId != null) || (this.exceptionReportId != null && !this.exceptionReportId.equals(other.exceptionReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ExceptionReport[ id=" + exceptionReportId + " ]";
    }
    
}
