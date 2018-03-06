// BuildingObjects.h
//
#ifndef BUILDINGOBJECTS_H
#define BUILDINGOBJECTS_H
#include <queue>

class Passenger
{
public:
	explicit Passenger(int startTime, int startFloor, int endFloor)
		: startTime(startTime), boardTime(startTime), endTime(startTime),
		  startFloor(startFloor), endFloor(endFloor)
	{
	} // end Passenger(int startTime, int startFloor, int endFloor)

	int getStartTime() const
	{
		return startTime;
	} // end getStartTime()

	int getBoardTime() const
	{
		return boardTime;
	} // end getBoardTime()

	void setBoardTime(int boardTime)
	{
		this->boardTime = boardTime;
	} // end setBoardTime()

	int getEndTime() const
	{
		return this->endTime;
	} // end setEndTime()

	void setEndTime(int endTime)
	{
		this->endTime = endTime;
	} // end setEndTime()

	int getStartFloor()  const
	{
		return startFloor;
	} // end getStartFloor()

	int getEndFloor() const
	{
		return endFloor;
	} // end getEndFloor()

	bool operator<(const Passenger& otherPassenger) const
	{
		return (this->startTime < otherPassenger.startTime);
	} // end operator<(const Passenger& otherPassenger)

	bool isAscending() const; // check direction of passenger
	std::string toString() const; // string representation
private:
	int startTime; // starting time
	int boardTime; // elevator boarding time
	int endTime; // ending time
	int startFloor; // starting floor
	int endFloor; // ending floor
}; // end class Passenger

class Floor
{
public:
	explicit Floor()
	{
	} // end Floor()

	std::queue<Passenger> getQueue() const
	{
		return this->queue;
	} // end getQueue()

	int numPassengers() const
	{
		return this->queue.size();
	}
	bool isEmpty() const; // check if floor is empty
	void addPassenger(Passenger& passenger); // add passenger
	void removePassenger(); // remove passenger
private:
	std::queue<Passenger> queue; // passenger queue
}; // end class Floor

class Elevator
{
public:
	enum class State
	{
		STOPPED,
		STOPPING,
		MOVING_UP,
		MOVING_DOWN
	};

	static const State INITIAL_STATE = State::STOPPED; // initial state
	static const unsigned int INITIAL_TIME = 0; // initial time
	static const unsigned int CAPACITY = 8; // elevator capacity
	static const int INITIAL_FLOOR = 0; // initial floor
	static const int NO_TARGET = -1; // no target flag

	explicit Elevator()
		: state(INITIAL_STATE), time(INITIAL_TIME),
		  floor(INITIAL_FLOOR), target(NO_TARGET)
	{
	} // end Elevator()

	State getState() const
	{
		return this->state;
	} // end getState()

	void setState(State state)
	{
		this->state = state;
	} // end setState(State state)

	unsigned int getTime() const
	{
		return this->time;
	} // end setTime(unsigned int time)

	void setTime(unsigned int time)
	{
		this->time = time;
	} // end setTime(unsigned int time)

	int getFloor() const
	{
		return this->floor;
	} // end getFloor()

	void setFloor(int floor)
	{
		this->floor = floor;
	} // end setFloor(int floor)

	int getTarget() const
	{
		return this->target;
	} // end getTarget()

	void setTarget(int target)
	{
		this->target = target;
	} // end setTarget(int target)

	int numPassengers() const
	{
		return this->passengers.size();
	}

	bool isEmpty() const; // check if elevator is empty
	bool isFull() const; // check if elevator is full
	void incrementTime(); // increment time in current state
	void goToStop(); // reset direction to let off first passenger
	void goToTarget(); // head to target
	bool addPassenger(Passenger& passenger, int time); // let on a passenger
	Passenger* removePassenger(); // let off a passenger
private:
	State state; // movement state
	unsigned int time; // time in current state
	int floor; // current or previous floor
	int target; // target floor
	std::vector<Passenger> passengers; // passengers
}; // end class Elevator

#endif
