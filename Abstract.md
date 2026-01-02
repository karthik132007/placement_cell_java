# **ABSTRACT**

The **Automated Airline Reservation and Airport Resource Allocation System** is a multi-stakeholder desktop application designed using object-oriented principles to simulate real-world airline operations.
The system focuses on **automatic flight scheduling and airport resource allocation**, where airlines initiate flight creation requests and the system engine autonomously assigns **time slots, runways, gates, and other operational resources** based on predefined airport policies and real-time availability.

Unlike traditional systems that rely on manual intervention by airport authorities, this system adopts a **rule-driven, system-managed approach**, ensuring conflict-free scheduling, efficient utilization of airport infrastructure, and consistent enforcement of operational constraints.
The platform supports three primary perspectives—**Airport Authority, Airline Manager, and Traveller**—each with clearly defined responsibilities and limited control, ensuring separation of concerns and realistic domain modeling.

Traveller interactions are intentionally simplified at the user interface level; however, each action triggers complex internal workflows such as **state-based booking management, dynamic pricing evaluation, automated notifications, and resource reallocation**.
The system emphasizes **automation, scalability, and maintainability**, making it suitable as a conceptual model for modern airline reservation and airport operation systems.

---

# **SYSTEM ROLES & RESPONSIBILITIES**

##  **1. Airport Authority (Policy & Governance Role)**

### Role Description

The Airport Authority acts as a **policy-defining entity**, not an operational controller. It does **not manually assign runways, gates, or time slots**. Instead, it defines the rules under which the system operates.

### Responsibilities

* Define airport operational policies
* Configure:

  * Number of runways and gates
  * Gate compatibility rules
  * Operating hours
  * Safety buffers between flights
* Monitor airport utilization via dashboards
* View congestion and operational status

### What the Airport Authority does NOT do

* Does not assign runways
* Does not allocate gates
* Does not schedule flights

> The authority governs *rules*, not *decisions*.

---

##  **2. Airline Manager (Business & Operations Role)**

### Role Description

The Airline Manager represents the airline’s operational and business interests. They initiate flight creation but **do not manually manage airport resources**.

### Responsibilities

* Create flight requests by providing:

  * Route
  * Aircraft
  * Preferred date/time
* Manage airline fleet and aircraft availability
* Monitor assigned schedules and resources
* Configure pricing strategies
* View operational reports and revenue statistics

### System Interaction

When a flight is created:

* The manager submits a request
* The system automatically assigns:

  * Time slot
  * Runway
  * Gate
* Assigned details are displayed as **read-only**

> The airline requests; the system decides.

---

## **3. Traveller (End-User Role)**

### Role Description

The Traveller interacts with the system only through **booking-related workflows**. Their authority is intentionally limited to ensure system integrity and realism.

### Responsibilities

* Search available flights
* Book seats
* Select seat preferences
* Cancel or reschedule bookings (subject to rules)
* Receive system notifications

### Internal Complexity (Hidden from UI)

Although traveller actions appear simple, each action triggers:

* Booking state transitions
* Pricing strategy evaluation
* Seat availability updates
* Refund and penalty calculations
* Notification events

> The traveller initiates actions; the system enforces rules.

---

## **4. System Engine (Core Intelligence Role)**

### Role Description

The System Engine is the **central decision-making component** responsible for all automated operations.

### Responsibilities

* Allocate time slots for flights
* Assign runways based on availability and safety rules
* Assign gates based on aircraft compatibility
* Detect and prevent scheduling conflicts
* Handle reallocation during delays or cancellations
* Trigger notifications to relevant stakeholders

### Key Characteristics

* Fully automated
* Rule-driven
* Real-time decision making
* No manual overrides by users

> This component replaces human-driven coordination with deterministic system logic.

---

##  **SYSTEM WORKFLOW SUMMARY**

1. Airline Manager requests flight creation
2. System Engine:

   * Validates aircraft availability
   * Allocates time slot
   * Assigns runway and gate
3. Flight is activated and opened for booking
4. Traveller books seats
5. System manages:

   * Booking lifecycle
   * Pricing
   * Notifications
6. Any changes (delay, cancellation) trigger:

   * Automatic resource reallocation
   * Traveller notifications

---

## **DESIGN PHILOSOPHY**

* Automation over manual control
* Separation of responsibilities
* Rule-driven decision making
* State-based workflows
* Real-world domain modeling

---

## **ONE-LINE DEFENS**

> “The system allows airlines to initiate flight creation, but all airport resource allocation and operational decisions are handled automatically by a centralized rule-driven engine.”
