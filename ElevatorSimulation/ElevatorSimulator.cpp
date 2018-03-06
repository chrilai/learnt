//ElevatorSimulation.cpp
//
#include <algorithm>
#include <fstream>
#include <iostream>
#include <memory>
#include <sstream>
#include "ElevatorSimulator.h"
using namespace std;

//***************************************************************************
// void readCSV( string filename, vector<Passenger> passengers )
// Purpose: Read passenger data from CSV file
// Postcondition: Passenger data is stored in vector
// Inputs: Passenger data values
//***************************************************************************
void ElevatorSimulator::readCSV(std::string filename)
{
	ifstream passengerFile(filename);
	if (passengerFile)
	{
		string headerRow;
		string passengerRow;
		unsigned double passengerValue;
		vector<unsigned int> passenger;
		getline(passengerFile, headerRow); // read header row
//		clog << headerRow << endl;
		while (getline(passengerFile, passengerRow)) // read passenger rows
		{
			try
			{
				stringstream ssPassenger(passengerRow);
				while (ssPassenger >> passengerValue)
				{
					passenger.push_back(passengerValue);
					if (ssPassenger.peek() == ',')
					{
						ssPassenger.ignore();
					}
				}
				// add passenger to simulator
				this->passengers.push_back(Passenger(passenger.at(0),
																 passenger.at(1),
																 passenger.at(2)));
//				clog << passenger.at(0) << ','
//					  << passenger.at(1) << ','
//					  << passenger.at(2) << endl;
			}
			catch (out_of_range& exception)
			{
				cerr << "Exception occurred: " << exception.what() << endl;
			}
			passenger.clear();
		}
		passengerFile.close();
		this->passengers.shrink_to_fit();
		// sort in descending order of start times
		sort(this->passengers.rbegin(), this->passengers.rend());
	}
} // end readCSV(std::string filename)

//***************************************************************************
// double getAverageWait( vector<Passenger> passengerArchive )
// Purpose: Get average wait time
//***************************************************************************
double ElevatorSimulator::getAverageWait() const
{
	int sum = 0;
	for_each(this->passengerArchive.begin(), this->passengerArchive.end(),
		[&](Passenger passenger)
		{
			sum += passenger.getBoardTime() - passenger.getStartTime();
		});
	return static_cast<double>(sum)/this->passengerArchive.size();
} // end getAverageWait()

//***************************************************************************
// double getAverageTravel( vector<Passenger> passengerArchive )
// Purpose: Get average travel time
//***************************************************************************
double ElevatorSimulator::getAverageTravel() const
{
	int sum = 0;
	for_each(this->passengerArchive.begin(), this->passengerArchive.end(),
		[&](Passenger passenger)
		{
			sum += passenger.getEndTime() - passenger.getBoardTime();
		});
	return static_cast<double>(sum)/this->passengerArchive.size();
} // end getAverageTravel()

//***************************************************************************
// double getAverageTime( vector<Passenger> passengerArchive )
// Purpose: Get average total trip time
//***************************************************************************
double ElevatorSimulator::getAverageTime() const
{
	int sum = 0;
	for_each(this->passengerArchive.begin(), this->passengerArchive.end(),
		[&](Passenger passenger)
		{
			sum += passenger.getEndTime() - passenger.getStartTime();
		});
	return static_cast<double>(sum)/this->passengerArchive.size();
} // end getAverageTime()

//***************************************************************************
// bool isComplete( vector<Passenger> passengers,
// 					  array<Floor, 1+TOP_FLOOR-BOTTOM_FLOOR> floors
//						  array<Elevator, ELEVATORS> elevators)
// Purpose: Check if elevator simulation is complete
//***************************************************************************
bool ElevatorSimulator::isComplete() const
{
	bool isComplete = true;
	// check for passengers yet to arrive at a floor
	if (!this->passengers.empty())
	{
		isComplete = false;
	}
	else
	{
		// check for passengers on floors
		for (auto it = this->floors.begin(); it != this->floors.end(); ++it)
		{
			if (!it->isEmpty())
			{
//				clog << "passenger(s) on floor" << endl;
				isComplete = false;
			}
		}
		// check for passengers on elevators
		for (auto it = this->elevators.begin(); it != this->elevators.end(); ++it)
		{
			if (!it->isEmpty())
			{
				isComplete = false;
//				clog << "passenger(s) on elevator" << endl;
			}
		}
	}
	return isComplete;
} // end isComplete()

//***************************************************************************
// int getStop( Elevator& elevator, vector<Passenger> passengers,
// 				 array<Floor, 1+TOP_FLOOR-BOTTOM_FLOOR> floors )
// Purpose: Algorithm to retrieve the next elevator stop
//***************************************************************************
int ElevatorSimulator::getStop(Elevator& elevator) const
{
	int stop = elevator.getTarget();
	// check if passengers need to be let off
	if (!elevator.isFull()) // no intermediate stops if full
	{
	}
	else if (elevator.getState() == Elevator::State::MOVING_UP)
	{
		for (auto i = elevator.getFloor(); i < elevator.getTarget(); ++i)
		{
			// check for waiting passengers
			if (!this->floors.at(i).isEmpty())
			{
				stop = i;
			}
			// check if passenger needs to be let off
			for (auto it = this->passengers.begin(); it != this->passengers.end(); ++it)
			{
				if (i == it->getEndFloor())
				{
					stop = i;
					break;
				}
			}
			if (stop == i)
			{
				break;
			}
		}
	}
	else if (elevator.getState() == Elevator::State::MOVING_DOWN)
	{
		for (auto i = elevator.getFloor(); i > elevator.getTarget(); --i)
		{
			// check for waiting passengers
			if (!this->floors.at(i).isEmpty())
			{
				stop = i;
			}
			// check if passenger needs to be let off
			for (auto it = this->passengers.begin(); it != this->passengers.end(); ++it)
			{
				if (i == it->getEndFloor())
				{
					stop = i;
					break;
				}
			}
			if (stop == i)
			{
				break;
			}
		}
	}
	return stop;
} // end getStop(Elevator& elevator)

//***************************************************************************
// void addPassenger( Passenger& passenger, Elevator& elevator,
// 						 vector<Passenger> passengers,
// 						 array<Floor, 1+TOP_FLOOR-BOTTOM_FLOOR> floors,
// 						 deque<int> targets )
// Purpose: Add passenger to floor
// Precondition: Floor exists
// Postcondition: Passenger is stored in queue for floor
//***************************************************************************
void ElevatorSimulator::addPassenger(Passenger& passenger)
{
	if (passenger.getStartFloor() == passenger.getEndFloor())
	{
		passengerArchive.push_back(passenger);
//		clog << "Passenger is already at desired floor: " << passenger.getStartFloor() << endl;
	}
	else
	{
		this->floors.at(passenger.getStartFloor()).addPassenger(passenger);
		this->targets.push_back(passenger.getStartFloor());
//		clog << "Passenger added to floor: " << passenger.getStartFloor() << endl;
	}
} // end addPassenger(Passenger& passenger)

//***************************************************************************
// void setTarget( Elevator& elevator, deque<int> targets )
// Purpose: Determine direction to move stopped elevator
//***************************************************************************
void ElevatorSimulator::setTarget(Elevator& elevator)
{
	if (!this->targets.empty()) // waiting passengers
	{
		if (this->targets.front() < elevator.getFloor()) // lower floor
		{
			elevator.setState(Elevator::State::MOVING_DOWN);
		}
		else // higher floors
		{
			elevator.setState(Elevator::State::MOVING_UP);
		}
		elevator.setTarget(this->targets.front());
		this->targets.pop_front();
		elevator.setTime(Elevator::INITIAL_TIME);
	}
} // end setTarget(Elevator& elevator)


//***************************************************************************
// void moveElevator( Elevator& elevator, Elevator::State state,
// 						 deque<int> targets, bool stepped,
// 						 unsigned int moveTime )
// Purpose: Move elevator one step
// Postcondition: Elevator is moved one step according to state
//***************************************************************************
void ElevatorSimulator::moveElevator(Elevator& elevator)
{
	switch(elevator.getState())
	{
		case Elevator::State::STOPPED:
//			clog << "stopped" << endl;
			// clear achieved targets
			remove(this->targets.begin(), this->targets.end(), elevator.getFloor());
			if (elevator.getFloor() == elevator.getTarget())
			{
				elevator.setTarget(Elevator::NO_TARGET);
			}
			if (elevator.getTarget() == Elevator::NO_TARGET)
			{
				// set target floor to let on passenger
				if (elevator.isEmpty())
				{
					setTarget(elevator);
//					clog << "Set target floor: " << elevator.getTarget() << endl;
				}
				// set target floor to let off passenger
				else
				{
					elevator.goToStop();
//					clog << "Heading to passenger stop." << endl;
				}
			}
			else // head to target floor
			{
				elevator.goToTarget();
//				clog << "Heading to target." << endl;
			}
			break;
		case Elevator::State::STOPPING:
//			clog << "stopping" << endl;
			if (elevator.getTime() >= STOP_TIME) // completed stop
			{
				elevator.setState(Elevator::State::STOPPED);
				elevator.setTime(Elevator::INITIAL_TIME);
//				clog << "Elevator stopped at floor: " << elevator.getFloor() << endl;
			}
			break;
		case Elevator::State::MOVING_UP:
		case Elevator::State::MOVING_DOWN:
//			clog << "moving" << endl;
			// check if elevator has reached stop, with stepped or constant speed
			if ((!this->stepped && elevator.getTime() >= this->moveTime) ||
				 (this->stepped &&
				  elevator.getTime() >= this->moveTime*abs(elevator.getFloor()-getStop(elevator))))

			{
				elevator.setFloor(getStop(elevator));
				elevator.setState(Elevator::State::STOPPING);
				elevator.setTime(Elevator::INITIAL_TIME);
			}
			break;
		default:
			break;
	}
} // end moveElevator()

//***************************************************************************
// void run( vector<Passenger> passengers,
// 			 array<Elevator, ELEVATORS> elevators,
// 			 array<Floor, 1+TOP_FLOOR-BOTTOM_FLOOR> floors,
// 			 vector<Passenger> passengerArchive, long int time )
// Purpose: Run elevator simulation
// Precondition: Passenger data is set
// Postcondition: Passenger data is stored in vector
// Inputs: Passenger data file
// Outputs: Elevator simulation results
//***************************************************************************
void ElevatorSimulator::run()
{
	this->time = START_TIME; // reset time
	// if needed, read passenger data
	while (this->passengers.empty())
	{
		string filename;
		cout << "No passenger data, please enter CSV file: " << endl;
		cin >> filename;
		readCSV(filename);
	}

	// run simulation
	cout << "Running Elevator Simulation..." << endl;
	while (!isComplete())
	{
		try
		{
//			clog << "Time: " << this->time << endl;
			// add passengers to floor at starting time
			while (!this->passengers.empty() &&
					 this->passengers.back().getStartTime() == time)
			{
				addPassenger(this->passengers.back());
				this->passengers.pop_back();
			}

			// each elevator performs an operation
			for (auto it = this->elevators.begin(); it != this->elevators.end(); ++it)
			{
				it->incrementTime(); // increment elevator time
				Floor& floor = this->floors.at(it->getFloor()); // get floor
//				clog << "Elevator is at Floor: " << it->getFloor() << endl;

				// let off a passenger (assume this action takes time = 1)
				Passenger* removedPassenger(it->removePassenger());
				if (removedPassenger)
				{
					removedPassenger->setEndTime(this->time);
					passengerArchive.push_back(*removedPassenger);
//					clog << "Let off passenger: " << removedPassenger->toString() << endl;
					continue;
				}
				// board a passenger (assume this action takes time = 1)
				if (!floor.isEmpty() &&
					 it->addPassenger(floor.getQueue().front(), this->time))
				{
//					clog << "Let on passenger: " << floor.getQueue().front().toString() << endl;
					floor.removePassenger(); // remove from floor
					continue;
				}

				// move the elevator
				moveElevator(*it);
			}
//			if (time > 100000) break;
			++this->time; // increment time
		}
		catch (bad_alloc& exception)
		{
			cerr << "Exception occurred: " << exception.what() << endl;
		}
		catch (out_of_range& exception)
		{
			cerr << "Exception occurred: " << exception.what() << endl;
		}
	}

	cout << "...Simulation Complete" << endl;
} // end run()

//***************************************************************************
// void printResults( )
// Purpose: Move elevator one step
// Postcondition: Elevator is moved one step according to state
//***************************************************************************
void ElevatorSimulator::printResults() const
{
	cout << "Elevator Simulation Results:" << '\n';
	cout << "Average Wait Time: " << getAverageWait() << '\n';
	cout << "Average Travel Time: " << getAverageTravel() << '\n';
	cout << "Average Travel Time: " << getAverageTime() << '\n';
	cout << endl;
} // end printResults()
