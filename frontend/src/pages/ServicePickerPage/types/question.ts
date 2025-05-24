export type AnswerValue =
  | { type: "reference"; value: string[] }
  | { type: "input"; value: number }
  | { type: "single-choice"; value: number | null }
  | { type: "multiple-choice"; value: number[] };

export interface QuestionBase {
  id: number;
  questionText: string;
  isRequired: boolean;
}

export interface ReferenceQuestion extends QuestionBase {
  type: "reference";
  referenceType: "regions" | "professions";
}

export interface InputQuestion extends QuestionBase {
  type: "input";
  validation?: { min: number; max: number };
  placeholder?: string;
}

export interface SingleChoiceQuestion extends QuestionBase {
  type: "single-choice";
  options: { id: number; text: string }[];
}

export interface MultipleChoiceQuestion extends QuestionBase {
  type: "multiple-choice";
  options: { id: number; text: string }[];
}

export type Question =
  | ReferenceQuestion
  | InputQuestion
  | SingleChoiceQuestion
  | MultipleChoiceQuestion;
