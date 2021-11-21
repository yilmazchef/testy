package be.intecbrussel.student.data.dto;


import java.sql.Timestamp;
import java.util.Objects;

public class ExamDto extends ADto {

	private String session;
	private String code;
	private Timestamp startTime;
	private Timestamp endTime;
	private UserDto organizer;
	private UserDto student;
	private TaskDto task;
	private Double score;
	private Boolean selected;
	private Boolean submitted;


	public UserDto getOrganizer() {

		return organizer;
	}


	public void setOrganizer( final UserDto organizer ) {

		this.organizer = organizer;
	}


	public void setOrganizerId( final String organizerId ) {

		this.organizer.setId( organizerId );
	}


	public ExamDto withOrganizer( final UserDto organizer ) {

		this.setOrganizer( organizer );
		return this;
	}


	public ExamDto withOrganizerId( final String organizerId ) {

		this.setOrganizerId( organizerId );
		return this;
	}


	public String getCode() {

		return code;
	}


	public void setCode( String code ) {

		this.code = code;
	}


	public ExamDto withCode( String code ) {

		this.setCode( code );
		return this;
	}


	public String getSession() {

		return session;
	}


	public void setSession( String session ) {

		this.session = session;
	}


	public Timestamp getStartTime() {

		return startTime;
	}


	public void setStartTime( Timestamp startTime ) {

		this.startTime = startTime;
	}


	public void setStartTime( String startTime ) {

		this.startTime = Timestamp.valueOf( startTime );
	}


	public Timestamp getEndTime() {

		return endTime;
	}


	public void setEndTime( Timestamp endTime ) {

		this.endTime = endTime;
	}


	public void setEndTime( String endTime ) {

		this.endTime = Timestamp.valueOf( endTime );
	}


	public UserDto getStudent() {

		return student;
	}


	public void setStudent( UserDto student ) {

		this.student = student;
	}


	public void setStudentId( String studentId ) {

		this.setStudent( new UserDto().withId( studentId ) );
	}


	public TaskDto getTask() {

		return task;
	}


	public void setTask( TaskDto task ) {

		this.task = task;
	}


	public void setTaskId( String taskId ) {

		this.setTask( new TaskDto().withId( taskId ) );
	}


	public Double getScore() {

		return score;
	}


	public void setScore( Double score ) {

		this.score = score;
	}


	public Boolean getSelected() {

		return selected;
	}


	public void setSelected( Boolean selected ) {

		this.selected = selected;
	}


	public Boolean getSubmitted() {

		return submitted;
	}


	public void setSubmitted( Boolean submitted ) {

		this.submitted = submitted;
	}


	public ExamDto withSubmitted( Boolean submitted ) {

		this.setSubmitted( submitted );
		return this;
	}


	public ExamDto withSelected( Boolean selected ) {

		this.setSelected( selected );
		return this;
	}


	public ExamDto withSession( String session ) {

		this.setSession( session );
		return this;
	}


	public ExamDto withStartTime( Timestamp startTime ) {

		this.setStartTime( startTime );
		return this;
	}


	public ExamDto withEndTime( Timestamp endTime ) {

		this.setEndTime( endTime );
		return this;
	}


	public ExamDto withStudentId( String studentId ) {

		this.setStudent( ( UserDto ) new UserDto().withId( studentId ) );
		return this;
	}


	public ExamDto withStudent( UserDto student ) {

		this.setStudent( student );
		return this;
	}


	public ExamDto withTaskId( String taskId ) {

		this.setTask( ( TaskDto ) new TaskDto().withId( taskId ) );
		return this;
	}


	public ExamDto withTask( TaskDto task ) {

		this.setTask( task );
		return this;
	}


	public ExamDto withScore( Double score ) {

		this.setScore( score );
		return this;
	}


	@Override
	public ExamDto withId( String id ) {

		super.setId( id );
		return this;
	}


	@Override
	public ExamDto withActive( Boolean active ) {

		super.setActive( active );
		return this;
	}


	@Override
	public String toString() {

		return this.getSession();
	}


	@Override
	public boolean equals( Object o ) {

		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof ExamDto ) ) {
			return false;
		}
		if ( !super.equals( o ) ) {
			return false;
		}
		ExamDto that = ( ExamDto ) o;
		return Objects.equals( getSession(), that.getSession() ) && getCode().equals( that.getCode() ) && getStudent().equals( that.getStudent() ) && getTask().equals( that.getTask() );
	}


	@Override
	public int hashCode() {

		return Objects.hash( super.hashCode(), getSession(), getCode(), getStudent(), getTask() );
	}

}
