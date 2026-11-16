# Team 281 — Programming Roadmap & Agent Instructions (2026–2027)

> **Scope:** This file contains Team 281’s coding rules, project structure, testing requirements, and ideas for the 2026–2027 FRC season. It is based on our programming roadmap and is meant to guide AI coding agents working in this repository. The original roadmap text is included at the end so nothing gets lost.
>
> **How to use this file:** Follow the rules that the team has already decided on. Some parts are still ideas, questions, or things we want to try. Don’t treat those as final decisions without checking first.

## Agent operating protocol

1. **Inspect before editing.** Read the current repository layout, existing `entech` and `robot` code, `RobotConstants`, IO types, command patterns, and tests. Do not invent classes, filenames, or interfaces when existing ones can be reused.
2. **Follow established constraints.** Apply the rules and architecture sections below; if a requested change conflicts with them, flag the conflict before changing the design.
3. **Protect hardware safety.** Keep hardware ownership singular, respect limits/switches, and use fail-safe behavior for CAN communication faults. Do not assume simulation behavior proves real robot safety.
4. **Prefer small, comprehensible changes.** Keep code organized, minimize repeated commands and cross-layer dependencies, write JavaDocs where useful, and leave humans able to debug without AI or internet access.
5. **Make logic testable without the robot.** Use injected dependencies and IO boundaries, with unit tests and simulation/integration tests where applicable.
6. **Validate before proposing deployment.** Run the available unit-test, Sonar, and simulation checks; state clearly what did and did not run. Actual deployment approval belongs to the team.
7. **Do not silently settle unresolved questions.** When a roadmap item says “consider,” “?”, “prototype,” or “research,” treat it as tentative and ask for a decision if it materially changes production code.
8. **Keep the roadmap intact.** If editing this document, do not delete original requirements or open topics without explicit team authorization. Update the active guidance and preserve the audit appendix.

## 1. Current architecture and organization

- Organize different hardware aspects into **subsystems**.
- **Commands represent actions** the robot performs (e.g., raising the elevator, running the intake).
- Avoid an oversized `RobotContainer`; split responsibilities into smaller classes (suggested in the roadmap: `RobotConstants`, Hardware Manager, `RobotIO`, etc.). Confirm actual class names against the repository.
- Keep hardware interactions isolated. Each piece of hardware should have one owning object/reference; other objects may request references where safe. Account for physical limits and switches.
- Maintain the two main code areas:
  - **`entech`**: reusable core robot features intended to carry over between seasons.
  - **`robot`**: code specific to this year’s robot.
- **Do not casually modify `entech` during the season.** The preferred plan is to update it at season end after evaluating which work is reusable. The roadmap acknowledges a possible exception for multiple cheap experimental projects, but insists that the current known-good `entech` must remain recoverable.
- **Prioritize defensive coding** and an understandable, navigable architecture.
- **Telemetry** is mentioned as an open question; do not invent a mandatory telemetry architecture.

## 2. Robot code structure — stated rules

These items were written in the source under **Robot Code Structure**:

1. Sensors are accessed from a global scope. **Needs clarification** alongside the ownership / IO-isolation rules below; do not add unrestricted global hardware access as an interpretation.
2. Robot **behaviors belong in commands**, not in subsystems.
3. `operatorInterface` is the **only location for control bindings**.
4. `RobotConstants` is the **only location for constants**; use it instead of magic numbers.
5. Subsystems must accept an **input object to accept commands**. Confirm the intended object type and method signature from actual team code before implementing.
6. Use an **`agents` folder** for documented behaviors. Do not assume an existing path without checking.
7. Use **organized autonomous paths in `pathlib`** (the source says “auth paths in pathlib”; precise directory naming should be confirmed against the repository).

## 3. Robot coding guidelines — stated rules

- **Log all exceptions.**
- **Do not mix wiring code with logic code.**
- **Do not mix different abstraction levels in the same method.**
- **Maximum 500 lines per file.** (An earlier source note has an unfinished “LIMIT HERE”; the later, explicit 500-line rule resolves the numeric limit.)
- **Never use `Thread.sleep`.**
- **No inheritance depth beyond 3 subclasses.**
- **Do not create threads.**
- **Log everything you will ever need.** (Original roadmap instruction.)
  - **Additional guidance:** Log information that may be needed for diagnosis, and keep logs actionable. This is advice on applying the rule, not a replacement for it.
- **Logic code must be testable off robot.**
- **Only subsystems or sensors may own hardware.** See IO-specific rules below for a potential ownership/terminology conflict to resolve.
- **No magic numbers; put constants in `RobotConstants`.**
- **Catch exceptions at the very last minute to make sure the robot does not crash.** (Original roadmap instruction.)
  - **Additional guidance:** Catch exceptions at the last appropriate boundary and avoid silently swallowing them. This is an implementation recommendation, not a change to the original rule.

### Hardware separation / simulation testability

The roadmap gives five explicit rules for maximizing code testable without a robot:

1. **Hardware only exists inside IO classes.**
2. **Sensor data comes in through Inputs; actuator commands go out through IO methods.**
3. **Constructors receive dependencies; they do not construct hardware.**
4. **Robot behavior does not depend on whether the environment is simulation; only the IO implementation changes.**
5. **If behavior can be phrased “when X happens, Y should happen,” write a laptop test for it.**

**Clarification needed:** “only subsystems or sensors may own hardware,” “hardware only exists inside IO classes,” and “sensors accessed from a global scope” appear together in the source. Preserve all three. Until the team settles how these fit together, avoid introducing new global hardware instances; follow established local project patterns and seek clarification for a redesign.

## 4. Tests and deployment gates

**Before deploying robot code:**

- Unit tests pass.
- Sonar checks/tests pass (ideally run locally).
- Robot code runs in simulation mode.

The roadmap defines unit tests as testing a **single isolated class, without the robot**. It also proposes broader integration tests using `DriverStationSim`, a movable simulated axis, and other simulated inputs. Do not claim to have executed a test or hardware check unless it actually ran.

## 5. Robot hardware and match-operation rules for 2027

- Put the **drive system on its own CAN bus**.
- Put **major other subsystems on separate CAN buses**.
- Subsystems must **detect loss of CAN communication automatically and fail safe**, so the robot remains operational where safely possible. Fail-safe specifics are not yet defined and require hardware/team review.
- Provide a **third debug controller**.
- **The debug controller must not go onto the field.**
- Seek to let the operator and driver **look at the driver/field throughout operation** (source phrasing: “try to let operator and driver look at the driver all the time”; intended target should be clarified).

## 6. Maintainability, navigation, and practices to avoid

- Avoid **large classes with too many responsibilities**; distribute work into smaller focused classes. Follow the 500-line limit and avoid unnecessarily deep hierarchies.
- Avoid **copy-pasted commands** that differ only slightly. Look for appropriate shared logic or parameterization without obscuring the distinct purpose of each action.
- Avoid **magic numbers**; centralize in `RobotConstants`. The source specifically likes its interface-based organization even if the file becomes large.
- Avoid needless reinvention when other teams' implementations exist, but recognize cases where finding/adapting external code is harder than implementing from first principles. This point is retained even though the source writer wondered whether AI changes the tradeoff.
- Make code **easy for non-programmers to understand and for anyone on the team to extend**.
- Improve discoverability so programmers do not need prior knowledge of where every function lives.
- **Improve communication between mechanical and programming teams**; make hardware specifications clear before coding.
- For autos, reduce confusing path proliferation, use **PathPlanner folders**, introduce **robot-specific vocabulary** for naming, and avoid copy-and-paste control structures.
- **Consider documenting all `entech` code with JavaDocs**, and add JavaDocs throughout the season. The motivation is maintainability in competition situations without Wi-Fi / AI and under agent context-window limitations. This was originally phrased as a recommendation, not an absolute requirement.

## 7. 2027 programming approach and roadmap work

These are **planned approaches / proposals**, not automatically approved tasks for the agent to implement:

- Develop strong **programming and hardware specifications**.
- Maintain written code-organization documentation, including this roadmap.
- Use **throwaway projects for prototyping**.
- Find or install an **architecture/layering-rules checker**.
- Investigate **Codex** as an agent.
- Install a **local plugin to run Sonar**.
- Maximize code that can be tested off robot (see the five testability rules above).
- Create a **2027 template repository**, using the `entech` package and the team's current documentation.
- Improve the process for **getting hardware specifications into code efficiently**.

### Before-season prototypes to investigate

- Alternate path planners, particularly the **“it can only run once”** bug.
- Self-disabling subsystem.
- Local Sonar.
- Full integration-test example with `DriverStationSim`, a movable simulated axis, etc.
- Triggers so subsystems can notify code when events happen.
- **Andrew's new lightweight loggers**.
- Agent-assisted questions over log files (**Codex**).
- An ultra-simple robot for prototyping.
- Live tuning **during a match**, with a more usable interface/wrapper for live-tunable variables.
- Command choosers for applications beyond autonomous, such as dashboard functionality/setups.
- Operator glasses / HUD.
- Nudge + button combination to adjust a setpoint: nudge until the target is good, then use a button to set the current position as the setpoint; alternative: double-press an existing action button to store that setpoint.
- LEDs synchronized to field state, with HUD as an alternative where LEDs create a competitive disadvantage.
- Rumble button.
- Double-tap button trigger (noted as new this year).
- **Epilog** (the source thinks Andrew's prototype uses it).
- Python on the Driver Station using NetworkTables, with arbitrary inputs/outputs controlled by a Raspberry Pi or Arduino.
- Ready-to-go LED subsystem.

### Combined prototype idea: simple position-based subsystem

Combine these previously listed ideas into a simple, position-based subsystem:

- Nudge + button setpoint.
- Triggers on arrival at a position.
- Epilog logging.
- SonarQube tests.
- Full simulation integration tests.
- Rumble button.

## 8. Future reference, AI research, and unresolved questions

These questions are deliberately **not** converted into mandatory implementation instructions:

- How should subsystems **check themselves**?
- How should **constant iteration** be used consistently? (Original says “How do we use constant iteration that is synonymous”; meaning unclear.)
- How can the team **switch between two different code implementations without a build**?
- How can **live tuning be changed in the middle of a match**?
- What precisely should the telemetry approach be?
- What further specifics are needed for hardware isolation and physical safety?
- What is the preferred pattern for **three or four similar-but-different commands** without creating redundant command junk? The original includes the rough notes “Hey I’m making a robot” and a research prompt about a command-driven robot; both remain in the source appendix.
- What precisely is the **global sensor access** rule, especially relative to injected IO and hardware ownership?
- Which class represents an input object for subsystems, and how does it relate to IO `Inputs`?
- What exactly does “organized auth paths in pathlib” refer to?
- How is **CAN communication failure** detected, and what does safe behavior mean for each subsystem?
- What layering checker / Sonar configuration should be adopted?
- Is the agent tool Codex, Claude Code, or both?

## 9. Team-member opinions preserved from the roadmap

**Likes:** stable `entech` / `robot` separation across seasons; a subsystem for each hardware piece; central `RobotConstants` organization using interfaces so constants are easy to find even when the file is large.

**Dislikes / improvement opportunities:** hard-to-find code; insufficient communication between mechanical and programming; confusing autos and many paths; underuse of PathPlanner folders and robot-specific vocabulary; copy-and-paste control structures.

## 10. Code examples supplied by the team

These examples are **preserved as reference material**, not guaranteed drop-in code for a future WPILib/GradleRIO version. Version placeholders and incomplete dependencies must be verified against the current project before use.

### Gradle: local Sonar example

```groovy
plugins {
    id "java"
    id "edu.wpi.first.GradleRIO" version "2026.x.x"
    id "org.sonarqube" version "7.5.0.8588"
}
```

### `DriverStationSim` integration test example

This demonstrates HAL initialization; scheduler cleanup; a fake `ArmIO`; actual `RobotContainer` construction; simulated USB operator button 5; teleop enablement; and assertion that the command outputs `0.60`. It assumes `ArmIO`, `ArmInput`, and the `RobotContainer(armIO)` constructor exist in the current project. **The original example below is retained in substance; formatting is normalized.**

```java
package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.simulation.GenericHIDSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArmOperatorTest {

    private FakeArmIO armIO;
    private GenericHIDSim operatorSim;

    @BeforeAll
    static void initializeHAL() {
        assertTrue(HAL.initialize(500, 0));
    }

    @BeforeEach
    void setup() {
        CommandScheduler scheduler = CommandScheduler.getInstance();

        scheduler.cancelAll();
        scheduler.unregisterAllSubsystems();
        scheduler.getDefaultButtonLoop().clear();
        scheduler.enable();

        armIO = new FakeArmIO();

        // Construct REAL RobotContainer.
        // This creates the real subsystem and real button bindings.
        new RobotContainer(armIO);

        // Simulate the USB HID on operator port 1.
        operatorSim = new GenericHIDSim(1);
        operatorSim.setButtonCount(12);

        // Pretend we're enabled in Teleop.
        DriverStationSim.setDsAttached(true);
        DriverStationSim.setAutonomous(false);
        DriverStationSim.setEnabled(true);
        DriverStationSim.notifyNewData();
    }

    @AfterEach
    void cleanup() {
        CommandScheduler scheduler = CommandScheduler.getInstance();

        scheduler.cancelAll();
        scheduler.unregisterAllSubsystems();
        scheduler.getDefaultButtonLoop().clear();

        DriverStationSim.setEnabled(false);
        DriverStationSim.notifyNewData();
    }

    @Test
    void operatorButton5RunsArmAt60Percent() {
        // Initially motor should be stopped.
        assertEquals(0.0, armIO.commandedMotorOutput, 0.001);

        // ---- ACT ----
        // Operator physically presses button 5.
        operatorSim.setRawButton(5, true);
        operatorSim.notifyNewData();

        // One 20 ms robot loop.
        CommandScheduler.getInstance().run();

        // ---- ASSERT ----
        assertEquals(0.60, armIO.commandedMotorOutput, 0.001);
    }

    private static class FakeArmIO implements ArmIO {
        double commandedMotorOutput = 0.0;

        @Override
        public void updateInputs(ArmInput ai) {
            ai.position = 0.0;
            ai.velocity = 0.0;
        }

        @Override
        public void setMotor(double output) {
            commandedMotorOutput = output;
        }
    }
}
```

## 11. Source preservation appendix — complete extracted original roadmap

The following is an archival **plain-text extraction of every page of the attached PDF**, kept so that rough notes, incomplete ideas, and original phrasing remain available. PDF line wrapping and form-feed page breaks may differ from the original visual layout. The structured sections above are the agent-usable interpretation; the appendix is the fidelity/audit copy. If the structured text appears to conflict with the source, flag it for human review.

````text
Programming Roadmap 281
26-27 Season
Facts
How do we organize code?

   -​ Subsystems for different hardware aspects of robot
   -​   Separate a big Robot Container class into smaller classes(Robot Constants,
        Hardware Manager, RobotIO, etc)
   -​   Commands are “actions” the robot performs, (ie raise the elevator, run the
        intake)
   -​   Try to keep hardware interactions isolated
            -​ *add specifics
            -​ One object should own one reference to each piece of hardware
            -​ Safe for other objects to ask for references to object
            -​ Safety - limits, switches (physical safety)
   -​   Telemetry?
   -​   There are two main folders, entech and robot
            -​ Entech is for core robot features that are the same from year to year, it is
                modified only at the end of each year to see what we developed in the
                current year that we want to keep for all other years
            -​ Robot is this year’s specific code.
            -​




How do we want to organize code? Ideal project structure?

   -​ Prioritize defense coding
   -​   Do not modify entech until end of year (or at least keep the current entech
        around so that we can replace it if need be, if we go with the “make a ton of
        different projects because projects are cheap” strategy then I can foresee
        changing Entech being beneficial, but I am still hesitant to be doing that
        frequently because it is such a core aspect of our team’s code)
   -​   I think we should consider adding or having ai add javadocs for all of entech and
        then adding javadocs as we go throughout the year so that it is easier on both ai
        and human programmers trying to understand. It would be bad if the humans

        were totally lost at an event with no wifi and so no ai or if the ai ran out of its
        context window or something like that
   -​   Testing
           -​ Unit Tests- Single class Isoloted. No robot




What should we avoid? Bad Practices?
   -​   Having big classes that do too much instead of spreading it out within smaller
        classes *specifics
            -​ Size limit for file(LIMIT HERE)
   -​   Magic Numbers (use RobotConstants instead)
   -​   Having many different commands that do the same thing but slightly modified.
        (copy pasted commands) *specifics
            -​ “Hey I’m making a robot”
            -​ Research : im doing command driven robot. in season having 3-4
               commands similar but w diff purpose how to sort out junk
   -​   Spending time reinventing what another team has already done
            -​ BUT also recognizing when it is more work to find something and tweak
               it for our implementation than it is to write it from first principles
                    -​ I typed those and then realized that ai makes the point moot, you
                        just have ai do it but I’ll leave it here just in case. I feel like I have
                        seen this happen a couple of times both ways (myself being
                        guilty too) so it seems like a good lesson to go ahead and put
                        here even if it is being made obsolete


Personal Opinion
Likes

   -​ Having the entech and robot folders separate and keeping entech season
      to season
   -​   Subsystems for each different piece of hardware
   -​   RobotConstants being a central location for constants instead of spread out as
        constants in the classes where they are used. The interfaces mean it is still well
        organized despite being very large and I always know that if I need to change a
        specific number 99% of the time its in RobotConstants.
   -​

Dislikes
   -​   Code can be a little bit disorienting if you are trying to find something specific
        you have to search for a while or know where it is ahead of time
   -​   Increase communication between mechanical and programming
   -​   Autos are often messed up and many different paths makes it hard to know
        what's what
            -​ Autos don’t make use of pathplanner folders to organize paths
            -​ We don’t use a lot of robot specific vocab to expedite the process
            -​ Lots of “copy and paste” code; control structures



What does a good robot look like?
   -​   Super organized code
           -​ Don't need to be a programmer to understand what code is doing +
               Anyone is able to add onto it
   -​   Defensive coding structure



Future Reference:
   -​   How do we get subsystems that check themselves?
   -​   How do we use constant iteration that is synonymous

        AI Research:
              If we want to split between twwo diff code implementations w/o build how
              do we do it
              How can we change live tuning in the middle of the match

MAKE REPO FOR 2027 TEMPLATE
  -​ Code in entech package’
  -​ Our current documentation


Getting hardware speci into code efficiently

Robot Code Structure
  ●​   Sensors are accessed from a global scope
  ●​   robot behaviors must be in commands, not in the subsystem
  ●​   operatorInterface must be the only place that control bindings exist
  ●​   RobotConstants must be the only place constants appear
  ●​   subsystems must accept an input object to accept commands
  ●​   agents folder for documented behaviors
  ●​   organized auth paths in pathlib


Robot Coding Guidelines

  ●​   log all exceptions
  ●​   no wiring code mixed with logic code
  ●​   no different levels of abstraction in a same method
  ●​   not more than 500 lines in one file
  ●​   no thread.sleep EVER
  ●​   not more than 3 subclasses deep
  ●​   no creating threads
  ●​   log everything you will ever need
  ●​   logic code should be testable off robot
  ●​   only subsystems or sensors may own hardware
  ●​   no magic numbers ( those go in RobotConstants )
  ●​   catch exceptions at the very last minute to make sure the robot does not crash


Robot Deployment: What must happen before a deploy
  ●​ Unit tests pass
  ●​ sonar tests pass ( ideally locally )
  ●​ robot runs in simulation mode



Programming Approaches for 2027 year
  ●​   very strong programming/ hardware specification
  ●​   written documentation on how the code should be organized
  ●​   this document
  ●​   code throw away projects for prototyping
  ●​   install/find some kind of layering rules checker
  ●​   use codex?
  ●​   install local plugin that runs sonar
  ●​   try to let operator and driver look at the driver all the time

  ●​ maximize code that can be tested off robot
       ○​ 1. Hardware only exists inside IO classes.
       ○​ 2. Sensors come in through Inputs; actuator commands go out
           through IO methods.
       ○​ 3. Constructors receive their dependencies; they don't create
           hardware.
       ○​ 4. Robot behavior never depends on whether we're in simulation.
           Only the IO implementation changes.
       ○​ 5. If a behavior can be stated as “when X happens, Y should
           happen,” write a laptop test for it.


Robot Rules for 2027 year
  ●​ drive system on its own can bus
  ●​ major other subsystems on separate can busses
  ●​ subsystems must auto-detect no can communication and fail safe ( robot must
     work )
  ●​ third debug controller.
  ●​ NO debug controller goes out onto the field


Prototypes to do before the year starts
  ●​   alternate path planners ( avoid the 'it can only run once bug)
  ●​   self-disabling subsystem
  ●​   local sonar
  ●​   full integration test example using DriverStationSim, an axis we can move, etc.
  ●​   triggers example so subsystems can tell us when things happen
  ●​   andrew's new lightweight loggers
  ●​   agent questions over log files ( codex )
  ●​   ultra simple robot for prototyping ( as simple as it can be )
  ●​   live tuning during a match ( and nicer interface/wrapper for live tunable vars )
  ●​   command choosers for things other than auto ( how can we use command
       choosers as dashboard functionality setups
  ●​   operator glasses/HUD
  ●​   nudge plus button combo to adjust a setpoint ( this could allow nudging to get to
       a point where we like, then a button that sets the setpoint to the current one.
            ○​ or, if there is a button already to trigger the action, double-press to set
                the current setpoint?
            ○​
  ●​   leds synchronized for field state ( HUD for cases where that's a competitive
       disadvantage )
  ●​   rumble button
  ●​   double tap button trigger ( new this year )

   ●​ Epilog ( i think Anrew's prototype uses this )
   ●​ python running on the driver station, using network tables, so that we can have
      arbitrary inputs, outputs, controlled by a pi or arduino
   ●​ ready-to-go LED subsystem

What would be cool



A simple position based subsystem, that we can combine
these prototypes
   ●​   nudge plus button setpoint
   ●​   triggers ( command at position arrival)
   ●​   epilog logging
   ●​   sonarqube tests
   ●​   full integration tests in simulation mode
   ●​   rumble button

Code Examples

Adding sonar to run locally
       plugins {
            id "java"
            id "edu.wpi.first.GradleRIO" version "2026.x.x"

             id "org.sonarqube" version "7.5.0.8588"
       }

Integration test using DriverStationSim

package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.simulation.GenericHIDSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArmOperatorTest {

     private FakeArmIO armIO;
     private GenericHIDSim operatorSim;

     @BeforeAll
     static void initializeHAL() {
         assertTrue(HAL.initialize(500, 0));
     }

     @BeforeEach
     void setup() {
         CommandScheduler scheduler =
             CommandScheduler.getInstance();

           scheduler.cancelAll();
           scheduler.unregisterAllSubsystems();

        scheduler.getDefaultButtonLoop().clear();
        scheduler.enable();

        armIO = new FakeArmIO();

        // Construct REAL RobotContainer.
        // This creates the real subsystem and real button
bindings.
        new RobotContainer(armIO);

        // Simulate the USB HID on operator port 1.
        operatorSim = new GenericHIDSim(1);
        operatorSim.setButtonCount(12);

        // Pretend we're enabled in Teleop.
        DriverStationSim.setDsAttached(true);
        DriverStationSim.setAutonomous(false);
        DriverStationSim.setEnabled(true);
        DriverStationSim.notifyNewData();
    }

    @AfterEach
    void cleanup() {
        CommandScheduler scheduler =
            CommandScheduler.getInstance();

        scheduler.cancelAll();
        scheduler.unregisterAllSubsystems();
        scheduler.getDefaultButtonLoop().clear();

        DriverStationSim.setEnabled(false);
        DriverStationSim.notifyNewData();
    }

    @Test
    void operatorButton5RunsArmAt60Percent() {

        // Initially motor should be stopped.
        assertEquals(0.0, armIO.commandedMotorOutput, 0.001);

        // ---- ACT ----
        // Operator physically presses button 5.
        operatorSim.setRawButton(5, true);
        operatorSim.notifyNewData();

        // One 20 ms robot loop.
        CommandScheduler.getInstance().run();

        // ---- ASSERT ----
        assertEquals(
            0.60,
            armIO.commandedMotorOutput,
            0.001
        );
    }

    private static class FakeArmIO implements ArmIO {
        double commandedMotorOutput = 0.0;

        @Override
        public void updateInputs(ArmInput ai) {
            ai.position = 0.0;
            ai.velocity = 0.0;
        }

        @Override
        public void setMotor(double output) {
            commandedMotorOutput = output;
        }
    }
}
````
