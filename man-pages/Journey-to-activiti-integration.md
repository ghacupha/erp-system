---
# The Journey to Activiti Integration

**Deployment:** `Mark IV No 5 (Ehud Series) Server ver 1.3.6`

## Unveiling Complexity at Scale

The true nature of any software system often reveals itself under the unforgiving spotlight of user acceptance testing (UAT). It was during the final UAT of our ERP system, just on the cusp of deployment, that the complexities we faced in managing large-scale workflows came to light.

### The 6000-Item Test

The ERP system, a robust platform built to handle a multitude of complex workflows, was put to the test with a staggering volume of 6000 asset registration items. The objective was clear: to validate the performance and reliability of our depreciation module under real-world conditions. It seemed like a routine test until it wasn't.

### The Out-of-Sync Depreciation Job

As the UAT progressed, something unexpected and unsettling unfolded. Despite rigorous testing and meticulous planning, we encountered a persistent issue—an out-of-sync depreciation job. While the system reported these jobs as complete, errors continued to cascade. The root cause lay in the intricacies of managing complex workflows involving thousands of assets.

### The Race Against Time

With the deployment date looming, we found ourselves in a race against time. The complexity of our workflows, compounded by the sheer volume of assets, presented a formidable challenge. Our initial design, though sound in theory, couldn't stand the test of scale and real-world complexity.

### The Options Explored

To address this critical issue, we began exploring various solutions. Among them were options like Kafka in ordered FIFO sequences and asynchronous locks (think ZooKeeper). Each of these solutions showed promise, offering the potential to bring order to the chaos.

### The Case for Activiti

However, as we delved deeper into these alternatives, we realized that they would require extensive testing and coverage, including navigating an estimated 100 decision points for each of the targeted 10,000 assets in the final deployment. Time was a luxury we couldn't afford.

### Activiti: The Ultimate Solution

Amidst the urgent need for a resolution, Activiti emerged as the ultimate solution. Its comprehensive feature set and scalability meant that it could gracefully handle the complexities of our ERP system. But what truly set Activiti apart was its ability to seamlessly integrate with our existing system, allowing us to address the issue without a major overhaul.

### The Integration Approach

Our integration approach was clear: deploy Activiti as a standalone or microservice system and connect it to our ERP system via APIs. This approach aligned perfectly with our philosophy of modular separation. It ensured that the Activiti code remained distinct from the ERP-system codebase, reducing the need for exhaustive test coverage and allowing for a timely delivery and deployment of the depreciation module.

### Conclusion

The journey to Activiti integration was one filled with challenges and revelations. The complexities that lay hidden beneath the surface only emerged when confronted with the demands of real-world UAT. In the end, the decision to embrace Activiti as our workflow management solution not only resolved the issues at hand but also set us on a path of scalability, efficiency, and seamless integration—a path that would lead our ERP system into the future.

Stay tuned for the next part, where the integration journey meets successful implementation.

---
