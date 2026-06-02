export interface ICrbAgentServiceType {
  id?: number;
  agentServiceTypeCode?: string;
  agentServiceTypeDetails?: string | null;
}

export class CrbAgentServiceType implements ICrbAgentServiceType {
  constructor(public id?: number, public agentServiceTypeCode?: string, public agentServiceTypeDetails?: string | null) {}
}

export function getCrbAgentServiceTypeIdentifier(crbAgentServiceType: ICrbAgentServiceType): number | undefined {
  return crbAgentServiceType.id;
}
