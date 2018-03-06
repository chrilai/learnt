//BuildingObjects.cpp
//
#include <algorithm>
#include <iostream>
#include <sstream>
#include "BuildingObjects.h"
using namespace std;

//***************************************************************************
// bool isAscending( int startFloor, int endFloor )
// Purpose: Check direction passenger needs to move in
//***************************************************************************
bool Passenger::isAscending() const
{
	return (this->endFloor - this->startFloor) > 0;
} // end isAscending()

//***************************************************************************
// void toString( int startTime, int boardTime, int endTime,
// 					int startFloor, int endFloor )
// Purpose: Return string representation of passenger
//***************************************************************************
string Passenger::toString() const
{
	stringstream passenger;
	passenger << startTime << "," << boardTime << "," << endTime << ","
				 << startFloor << "," << endFloor;
	return passenger.str();
} // end toString()

//***************************************************************************
// bool isEmpty( queue<Passenger> queue )
// Purpose: Check if floor is empty
//***************************************************************************
bool Floor::isEmpty() const
{
	return queue.empty();
} // end isEmpty()

//***************************************************************************
// void addPassenger( Passenger& passenger, queue<Passenger> queue)
// Purpose: Add passenger to floor
// Postcondition: Passenger is added to back of queue
//***************************************************************************
void Floor::addPassenger(Passenger& passenger)
{
	try
	{
		queue.push(passenger);
	}
	catch(bad_alloc& exception)
	{
		cerr << "Exception occurred: " << exception.what() << endl;
	}
} // end addPassenger(Passenger passenger)

//***************************************************************************
// void removePassenger( queue<Passenger> queue)
// Purpose: Remove first passenger to floor
// Postcondition: First passenger is removed from floor
//***************************************************************************
void Floor::removePassenger()
{
	if (!this->queue.empty())
	{
		this->queue.pop();
	}
} // end removePassenger()

//***************************************************************************
// bool isEmpty( vector<Passenger> passengers )
// Purpose: Check if elevator is empty
//***************************************************************************
bool Elevator::isEmpty() const
{
	return this->passengers.empty();
} // end isEmpty()

//***************************************************************************
// bool isFull( vector<Passenger> passengers )
// Purpose: Check if elevator is full
//***************************************************************************
bool Elevator::isFull() const
{
	return this->passengers.size() >= CAPACITY;
} // end isFull()

//***************************************************************************
// void incrementTime( unsigned int time )
// Purpose: Increment time in current state
//***************************************************************************
void Elevator::incrementTime()
{
	++this->time;
} // end incrementTime()

//***************************************************************************
// void goToStop( vector<Passenger> passengers, State state, unsigned int time )
// Purpose: Reset movement of elevator to let off first passenger
// Postcondition: Direction is set
//***************************************************************************
void Elevator::goToStop()
{
	if (!isEmpty())
	{
		// sort passengers according to start time
		sort(this->passengers.begin(),this->passengers.end());
		if (this->passengers.front().isAscending())
		{
			this->state = State::MOVING_UP;
		}
		else
		{
			this->state = State::MOVING_DOWN;
		}
		this->target = this->passengers.front().getEndFloor();
		this->time = INITIAL_TIME;
	}
} // end goToStop()

//***************************************************************************
// void goToTarget( int floor, int target, State state, unsigned int time )
// Purpose: Reset movement of elevator to head to target
// Postcondition: Direction is reset
//***************************************************************************
void Elevator::goToTarget()
{
	if (this->target != Elevator::NO_TARGET)
	{
		if (this->floor < this->target)
		{
			this->state = State::MOVING_UP;
		}
		else
		{
			this->state = State::MOVING_DOWN;
		}
		this->time = INITIAL_TIME;
	}
} // end goToTarget()

//***************************************************************************
// bool addPassenger( Passenger* passenger, vector<Passenger> passengers)
// Purpose: Let on a passenger from the current floor
// Postcondition: Passenger is added to elevator
//***************************************************************************
bool Elevator::addPassenger(Passenger& passenger, int time)
{
	bool added = false;
	if (this->state == State::STOPPED && !isFull() &&
		 passenger.getStartFloor() == this->floor)
	{
		passenger.setBoardTime(time);
		this->passengers.push_back(passenger);
		added = true;
	}
	return added;
} // end addPassenger()

//***************************************************************************
// bool removePassenger( Passenger* passenger, vector<Passenger> passengers)
// Purpose: Let off a passenger at the desired floor
// Postcondition: Passenger removed from elevator
//***************************************************************************
Passenger* Elevator::removePassenger()
{
	Passenger* passenger = 0;
	if (this->state == State::STOPPED)
	{
		for (auto it = this->passengers.begin(); it != this->passengers.end(); ++it)
		{
			if (it->getEndFloor() == this->floor)
			{
				swap(*it, this->passengers.back());
				passenger = &(this->passengers.back());
				this->passengers.pop_back();
				break;
			}
		}
	}
	return passenger;
} // end removePassenger()
